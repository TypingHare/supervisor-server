package supervisor.common

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder

class PasswordEncryptionUtility(
    encryptionSecret: String,
    saltLength: Int,
    iterationCount: Int
) {
    private val passwordEncoder: PasswordEncoder = Pbkdf2PasswordEncoder(
        encryptionSecret,
        saltLength,
        iterationCount,
        Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256
    )

    fun encryptPassword(rawPassword: CharSequence): String =
        passwordEncoder.encode(rawPassword)

    fun isPasswordValid(
        rawPassword: CharSequence,
        hashedPassword: String
    ): Boolean =
        passwordEncoder.matches(rawPassword, hashedPassword)
}