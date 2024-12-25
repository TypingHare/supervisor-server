package supervisor.controller

import graphql.GraphQLContext
import jakarta.servlet.http.Cookie
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import supervisor.common.CookieUtil
import supervisor.common.JwtUtil
import supervisor.common.constant.CookieKey
import supervisor.model.User
import supervisor.service.UserService

@Controller
class UserController(
    private val userService: UserService,
    private val jwtUtil: JwtUtil,
    private val cookieUtil: CookieUtil,
) {
    @QueryMapping
    fun userById(@Argument id: Long): User = userService.getUserById(id)

    @MutationMapping
    fun createUser(
        @Argument username: String,
        @Argument email: String,
        @Argument password: String,
        context: GraphQLContext
    ): User = userService.createUser(username, email, password).apply {
        setTokenCookie(id, context)
    }

    @MutationMapping
    fun signIn(
        @Argument emailOrUsername: String,
        @Argument password: String,
        context: GraphQLContext
    ): User = userService.signIn(emailOrUsername, password).apply {
        setTokenCookie(id, context)
    }

    private fun setTokenCookie(userId: Long, context: GraphQLContext) {
        val token = jwtUtil.generateToken(userId)
        Cookie(CookieKey.USER_TOKEN, token).let { cookieUtil.set(context, it) }
    }
}