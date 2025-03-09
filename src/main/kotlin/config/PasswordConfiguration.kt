package supervisor.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder

/**
 * Security configuration for user password encryption and hashing.
 */
@Configuration
@PropertySource("classpath:password.properties")
class PasswordConfiguration {
    @Value("\${password.encryption.secret}")
    private val passwordEncryptionSecret: String = ""

    @Value("\${password.encryption.salt-length}")
    private val passwordEncryptionSaltLength: Int = 0

    @Value("\${password.encryption.iterations}")
    private val passwordEncryptionIterations: Int = 0

    @Bean
    fun PasswordEncoder(): PasswordEncoder {
        return Pbkdf2PasswordEncoder(
            passwordEncryptionSecret,
            passwordEncryptionSaltLength,
            passwordEncryptionIterations,
            Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256
        )
    }
}