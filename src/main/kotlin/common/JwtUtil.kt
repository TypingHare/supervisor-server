package supervisor.common

import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.*
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit

@Component
class JwtUtil(
    private val jwtEncoder: JwtEncoder,
    private val jwtDecoder: JwtDecoder,
    private val jwtValidDurationInMinutes: Long
) {
    fun generateToken(userId: Long): String {
        val now = Instant.now()
        val expireAt =
            now.plus(jwtValidDurationInMinutes, ChronoUnit.MINUTES)
        val claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(expireAt)
            .claim(ClaimKey.UID, userId)
            .build()

        val headers = JwsHeader.with(MacAlgorithm.HS256).build()
        return jwtEncoder
            .encode(JwtEncoderParameters.from(headers, claims))
            .tokenValue
    }

    fun validateToken(token: String): JwtWrapper {
        return try {
            return JwtWrapper(true, jwtDecoder.decode(token))
        } catch (_: JwtException) {
            JwtWrapper(false)
        }
    }

    class JwtWrapper(val isValid: Boolean, val jwt: Jwt? = null)

    object ClaimKey {
        const val UID = "uid"
    }
}