package supervisor.interceptor

import jakarta.servlet.http.Cookie
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import supervisor.common.constant.ContextKey

@Component
class CookieInterceptor : WebGraphQlInterceptor {
    override fun intercept(
        request: WebGraphQlRequest,
        chain: WebGraphQlInterceptor.Chain
    ): Mono<WebGraphQlResponse?> {
        return chain.next(request).doOnNext { response ->
            val cookieList = response
                .executionInput
                .graphQLContext.getOrDefault(
                    ContextKey.COOKIE_LIST,
                    mutableListOf<Cookie>()
                )

            for (cookie in cookieList) {
                ResponseCookie.from(cookie.name, cookie.value).build().let {
                    response.responseHeaders.add(
                        HttpHeaders.SET_COOKIE,
                        it.toString()
                    )
                }
            }
        }
    }
}