package supervisor.config

import graphql.GraphQLContext
import graphql.schema.GraphQLScalarType
import jakarta.servlet.http.Cookie
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer
import supervisor.common.ContextKey
import supervisor.graphql.DateTimeScalar

@Configuration
class GraphQLConfiguration {
    @Bean
    fun dateTimeScalar(): GraphQLScalarType =
        GraphQLScalarType.newScalar()
            .name("DateTime")
            .description("A custom scalar to handle ISO-8601 DateTime")
            .coercing(DateTimeScalar())
            .build()

    @Bean
    fun runtimeWiringConfigurer(dateTimeScalar: GraphQLScalarType) =
        RuntimeWiringConfigurer { it.scalar(dateTimeScalar) }

    @Bean
    fun getCookies(): (GraphQLContext) -> MutableList<Cookie> =
        { context ->
            context.computeIfAbsent(ContextKey.COOKIE_LIST) {
                mutableListOf()
            }
        }

    @Bean
    fun setCookie(
        getCookies: (GraphQLContext) -> MutableList<Cookie>
    ): (GraphQLContext, Cookie) -> Unit =
        { context, cookie ->
            getCookies(context).add(cookie.apply {
                this.maxAge = 3600
                this.path = "/"
            })
        }
}