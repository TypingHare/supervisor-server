package supervisor.controller

import graphql.GraphQLContext
import jakarta.servlet.http.Cookie
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import supervisor.common.CookieKey
import supervisor.model.User
import supervisor.service.JwtService
import supervisor.service.UserService

@Controller
class UserController(
    private val userService: UserService,
    private val jwtService: JwtService,
    private val setCookie: (GraphQLContext, Cookie) -> Unit
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
        val token = jwtService.generateToken(userId)
        val cookie = Cookie(CookieKey.USER_TOKEN, token)
        setCookie(context, cookie)
    }
}