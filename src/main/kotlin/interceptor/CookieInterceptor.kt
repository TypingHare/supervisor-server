package supervisor.interceptor

import jakarta.servlet.http.Cookie
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import supervisor.common.ContextKey

/**
 * Intercepts GraphQL requests to process and apply cookies from the GraphQL
 * context to the HTTP response.
 *
 * This interceptor extracts cookies stored in the GraphQL context under the
 * {@code ContextKey.COOKIE_LIST} key and adds them to the HTTP response headers
 * as {@code Set-Cookie} headers. This allows GraphQL resolvers to set cookies
 * that will be sent back to the client.
 *
 * The interceptor runs after the GraphQL execution has completed but before the
 * response is sent to the client, ensuring all cookies added during resolver
 * execution are properly included in the response.
 *
 * @see WebGraphQlInterceptor
 * @see Cookie
 * @see ResponseCookie
 */
@Component
class CookieInterceptor : WebGraphQlInterceptor {
    /**
     * Intercepts the GraphQL request and response chain to process cookies.
     *
     * @param request The incoming GraphQL request
     * @param chain The interceptor chain
     * @return A Mono containing the GraphQL response with cookies added to the
     *         response headers
     */
    override fun intercept(
        request: WebGraphQlRequest,
        chain: WebGraphQlInterceptor.Chain
    ): Mono<WebGraphQlResponse> =
        chain.next(request).doOnNext { response ->
            val cookies = response
                .executionInput
                .graphQLContext
                .getOrDefault(ContextKey.COOKIE_LIST, mutableListOf<Cookie>())

            for (cookie in cookies) {
                ResponseCookie.from(cookie.name, cookie.value).build().let {
                    response.responseHeaders.add(
                        HttpHeaders.SET_COOKIE,
                        it.toString()
                    )
                }
            }
        }
}