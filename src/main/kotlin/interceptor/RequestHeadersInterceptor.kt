package supervisor.interceptor

import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import supervisor.common.ContextKey
import supervisor.common.HeadersKey

/**
 * Intercepts GraphQL requests to extract HTTP headers and add them to the
 * GraphQL context.
 *
 * This interceptor maps specific HTTP headers from the incoming request to
 * corresponding keys in the GraphQL context, making header values accessible to
 * resolvers during query execution. The primary purpose is to provide
 * authentication and authorization information (like tokens) to the GraphQL
 * execution layer without exposing implementation details.
 *
 * Currently configured to map:
 * - The "Authorization" header to the token context key
 *
 * If a header is not present in the request, a default empty value is used.
 *
 * @see WebGraphQlInterceptor
 * @see WebGraphQlRequest
 * @see HeadersKey
 * @see ContextKey
 */
@Component
class RequestHeadersInterceptor : WebGraphQlInterceptor {
    override fun intercept(
        request: WebGraphQlRequest,
        chain: WebGraphQlInterceptor.Chain
    ): Mono<WebGraphQlResponse?> {
        val headerKeyToContextKey = mapOf(
            HeadersKey.AUTHORIZATION to ContextKey.TOKEN,
        )

        val getRequestValue: (String) -> String = { headerKey ->
            request.headers.getFirst(headerKey) ?: DEFAULT_VALUE
        }

        val map = headerKeyToContextKey.toList()
            .associate { (headerKey, contextKey) ->
                contextKey to getRequestValue(headerKey)
            }

        request.configureExecutionInput { _, builder ->
            builder.graphQLContext(map).build()
        }

        return chain.next(request)
    }

    companion object {
        private const val DEFAULT_VALUE = ""
    }
}