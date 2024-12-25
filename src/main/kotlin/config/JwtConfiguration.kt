package supervisor.config

import com.nimbusds.jose.jwk.source.ImmutableSecret
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Configuration
@PropertySource("classpath:jwt.properties")
class JwtConfiguration {
    @Value("\${jwt.secret}")
    private val jwtSecret: String = ""

    @Value("\${jwt.valid-duration-in-minutes}")
    private val jwtValidDurationInMinutes: Long = 60

    @Bean
    fun jwtSecretKey(): SecretKey =
        SecretKeySpec(jwtSecret.toByteArray(), "HmacSHA256")

    @Bean
    fun jwtEncoder(jwtSecretKey: SecretKey): JwtEncoder =
        NimbusJwtEncoder(ImmutableSecret(jwtSecretKey))

    @Bean
    fun jwtDecoder(jwtSecretKey: SecretKey): JwtDecoder =
        NimbusJwtDecoder
            .withSecretKey(jwtSecretKey)
            .macAlgorithm(MacAlgorithm.HS256)
            .build()

    @Bean
    fun jwtValidDurationInMinutes(): Long = jwtValidDurationInMinutes
}