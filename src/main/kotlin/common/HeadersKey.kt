package supervisor.common

/**
 * Constants for HTTP request header names. These keys are used to access
 * standard HTTP headers like Authorization when processing incoming requests.
 *
 * @see graphql.GraphQLContext
 */
object HeadersKey {
    const val AUTHORIZATION = "Authorization"
}