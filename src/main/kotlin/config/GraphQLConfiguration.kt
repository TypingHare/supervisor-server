package supervisor.config

import graphql.schema.GraphQLScalarType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer
import supervisor.graphql.scalar.DateTimeScalar

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
    fun runtimeWiringConfigurer(
        dateTimeScalar: GraphQLScalarType
    ) = RuntimeWiringConfigurer { wiringBuilder ->
        wiringBuilder.scalar(dateTimeScalar)
    }
}