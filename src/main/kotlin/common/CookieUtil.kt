package supervisor.common

import graphql.GraphQLContext
import jakarta.servlet.http.Cookie
import org.springframework.stereotype.Component
import supervisor.common.constant.ContextKey

@Component
class CookieUtil {
    fun set(context: GraphQLContext, cookie: Cookie) {
        getCookieList(context).add(activate(cookie))
    }

    fun getCookieList(context: GraphQLContext): MutableList<Cookie> {
        return context
            .getOrDefault(ContextKey.COOKIE_LIST, mutableListOf<Cookie>())
            .apply { context.put(ContextKey.COOKIE_LIST, this) }
    }

    private fun activate(cookie: Cookie): Cookie {
        cookie.maxAge = 3600
        cookie.path = "/"
        return cookie
    }
}