package supervisor.common.constant

/**
 * Contains cookie key constants used for HTTP response headers. When these keys
 * are used with "Set-Cookie" headers, browsers will store and manage the
 * corresponding cookies.
 */
object CookieKey {
    const val USER_TOKEN = "user_token"
}