package supervisor.service

import jakarta.mail.internet.AddressException
import jakarta.mail.internet.InternetAddress
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Service
import supervisor.common.JwtUtil
import supervisor.common.PasswordEncryptionUtility
import supervisor.exception.ResourceInUseException
import supervisor.exception.ResourceNotFoundException
import supervisor.exception.ValidationException
import supervisor.model.User
import supervisor.repository.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository,
    private val entityManager: EntityManager,
    private val passwordEncryptionUtility: PasswordEncryptionUtility,
    private val jwtUtil: JwtUtil
) {
    @Throws(ResourceNotFoundException::class)
    fun getUserById(id: Long): User = userRepository.findById(id)
        .orElseThrow { ResourceNotFoundException.USER }

    @Throws(ResourceNotFoundException::class)
    fun getUserByEmail(email: String): User = userRepository.findByEmail(email)
        .orElseThrow { ResourceNotFoundException.USER }

    @Throws(ResourceNotFoundException::class)
    fun getUserByUsername(username: String): User =
        userRepository.findByUsername(username)
            .orElseThrow { ResourceNotFoundException.USER }

    fun getUserReferenceById(id: Long): User =
        entityManager.getReference(User::class.java, id)

    @Throws(ValidationException::class)
    fun getUserByToken(token: String): User {
        if (token.isBlank()) {
            throw ValidationException.USER_TOKEN_MISSING
        }

        val jwtWrapper = jwtUtil.validateToken(token)
        if (!jwtWrapper.isValid || jwtWrapper.jwt == null) {
            throw ValidationException.USER_TOKEN_INVALID
        }

        val userId = jwtWrapper.jwt.getClaim<Long>(JwtUtil.ClaimKey.UID)
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

        val authString = passwordEncryptionUtility.encryptPassword(password)

        return userRepository.save(User().apply {
            this.username = username
            this.email = email
            this.authString = authString
        })
    }

    @Throws(ResourceNotFoundException::class, ValidationException::class)
    fun signIn(usernameOrEmail: String, password: String): User =
        when (isEmailValid(usernameOrEmail)) {
            true -> signInWithEmail(usernameOrEmail, password)
            false -> signInWithUsername(usernameOrEmail, password)
        }

    @Throws(ResourceNotFoundException::class, ValidationException::class)
    private fun signInWithEmail(email: String, password: String): User =
        getUserByEmail(email).apply {
            if (!isPasswordCorrect(password, authString)) {
                throw ValidationException.USER_PASSWORD
            }
        }

    @Throws(ResourceNotFoundException::class, ValidationException::class)
    private fun signInWithUsername(username: String, password: String): User =
        getUserByUsername(username).apply {
            if (!isPasswordCorrect(password, authString)) {
                throw ValidationException.USER_PASSWORD
            }
        }

    private fun isPasswordCorrect(
        password: String,
        authString: String
    ): Boolean =
        passwordEncryptionUtility.isPasswordValid(
            password,
            authString
        )

    private fun isEmailValid(email: String): Boolean {
        return try {
            InternetAddress(email).validate()
            true
        } catch (_: AddressException) {
            false
        }
    }
}