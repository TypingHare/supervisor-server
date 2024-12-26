package supervisor.common.constant

/**
 * Constants for GraphQLContext keys used throughout the request lifecycle.
 * GraphQLContext is Spring GraphQL per-request context store that maintains
 * a key-value map of request-scoped data.
 *
 * @see graphql.GraphQLContext
 */
object ContextKey {
    const val COOKIE_LIST = "cookieList"
    const val TOKEN = "token"
}