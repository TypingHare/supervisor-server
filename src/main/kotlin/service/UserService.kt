package supervisor.service

import jakarta.mail.internet.AddressException
import jakarta.mail.internet.InternetAddress
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import supervisor.exception.ResourceInUseException
import supervisor.exception.ResourceNotFoundException
import supervisor.exception.ValidationException
import supervisor.model.User
import supervisor.repository.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder
) {
    @Throws(ResourceNotFoundException::class)
    fun getUserById(id: Long): User =
        userRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException.USER }

    @Throws(ResourceNotFoundException::class)
    fun getUserByEmail(email: String): User =
        userRepository
            .findByEmail(email)
            .orElseThrow { ResourceNotFoundException.USER }

    @Throws(ResourceNotFoundException::class)
    fun getUserByUsername(username: String): User =
        userRepository
            .findByUsername(username)
            .orElseThrow { ResourceNotFoundException.USER }

    @Throws(ValidationException::class)
    fun getUserByToken(token: String): User {
        if (token.isBlank()) {
            throw ValidationException.USER_TOKEN_MISSING
        }

        val jwtWrapper = jwtService.validateToken(token)
        if (!jwtWrapper.isValid || jwtWrapper.jwt == null) {
            throw ValidationException.USER_TOKEN_INVALID
        }

        val userId = jwtWrapper.jwt.getClaim<Long>(JwtService.ClaimKey.UID)
        return getUserById(userId)
    }

    @Throws(ResourceInUseException::class)
    fun createUser(username: String, email: String, password: String): User {
        if (userRepository.findByUsername(username).isPresent) {
            throw ResourceInUseException.USERNAME
        }

        if (userRepository.findByEmail(email).isPresent) {
            throw ResourceInUseException.EMAIL
        }

        val authString = passwordEncoder.encode(password)
        return userRepository.save(User().apply {
            this.username = username
            this.email = email
            this.authString = authString
        })
    }

    @Throws(ResourceNotFoundException::class, ValidationException::class)
    fun signIn(usernameOrEmail: String, password: String): User {
        val user = when (isEmail(usernameOrEmail)) {
            true -> getUserByEmail(usernameOrEmail)
            false -> getUserByUsername(usernameOrEmail)
        }

        if (!verifyPassword(password, user.authString)) {
            throw ValidationException.USER_PASSWORD
        }

        return user
    }

    private fun verifyPassword(
        password: String,
        authString: String
    ): Boolean = passwordEncoder.matches(password, authString)

    private fun isEmail(email: String): Boolean =
        try {
            InternetAddress(email).validate()
            true
        } catch (_: AddressException) {
            false
        }
}