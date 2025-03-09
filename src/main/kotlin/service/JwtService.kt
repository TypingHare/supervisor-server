package supervisor.service

import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.*
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class JwtService(
    private val jwtEncoder: JwtEncoder,
    private val jwtDecoder: JwtDecoder,
    private val jwtValidDurationInMinutes: Long,
) {
    /**
     * Generates a token.
     *
     * This method uses `jwtEncoder` to generate a JWT. The following entries
     * are added to the claim:
     *
     *     "uid" => userId
     *
     * @see ClaimKey
     */
    fun generateToken(userId: Long): String {
        val now = Instant.now()
        val expireAt = now.plusSeconds(jwtValidDurationInMinutes * 60L)
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

    /**
     * Validates a token and returns a JWT wrapper.
     *
     * This method uses `jwtDecoder` to decode the given token string. If it
     * throws a `JwtException`, it returns a JWT wrapper with `isValid` set to
     * false. Otherwise, it returns a JWT wrapper with the decoded JWT.
     *
     * @see JwtWrapper
     */
    fun validateToken(token: String): JwtWrapper {
        return try {
            return JwtWrapper(true, jwtDecoder.decode(token))
        } catch (_: JwtException) {
            JwtWrapper(false)
        }
    }

    data class JwtWrapper(val isValid: Boolean, val jwt: Jwt? = null)

    object ClaimKey {
        const val UID = "uid"
    }
}