package supervisor.interceptor

import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class RequestHeadersInterceptor(
    private val headerToContextKeyMap: Map<String, String>
) : WebGraphQlInterceptor {
    override fun intercept(
        request: WebGraphQlRequest,
        chain: WebGraphQlInterceptor.Chain
    ): Mono<WebGraphQlResponse?> {
        val map = headerToContextKeyMap.toList()
            .associate { (headerName, contextKey) ->
                contextKey to (request.headers.getFirst(headerName)
                    ?: DEFAULT_VALUE)
            }

        request.configureExecutionInput { _, builder ->
            builder.graphQLContext(map).build()
        }

        return chain.next(request)
    }

    companion object {
        const val DEFAULT_VALUE = ""
    }
}