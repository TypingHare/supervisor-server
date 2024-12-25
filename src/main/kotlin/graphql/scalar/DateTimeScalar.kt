package supervisor.graphql.scalar

import graphql.GraphQLContext
import graphql.execution.CoercedVariables
import graphql.language.StringValue
import graphql.language.Value
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DateTimeScalar : Coercing<LocalDateTime, String> {
    private val formatter = DateTimeFormatter.ISO_DATE_TIME

    override fun serialize(
        dataFetcherResult: Any,
        graphQLContext: GraphQLContext,
        locale: Locale
    ): String {
        return if (dataFetcherResult is LocalDateTime) {
            dataFetcherResult.format(formatter)
        } else {
            throw CoercingSerializeException("Expected a LocalDateTime object.")
        }
    }

    override fun parseValue(
        input: Any,
        graphQLContext: GraphQLContext,
        locale: Locale
    ): LocalDateTime? {
        return try {
            LocalDateTime.parse(input.toString(), formatter)
        } catch (_: Exception) {
            throw CoercingParseValueException("Invalid DateTime format.")
        }
    }

    override fun parseLiteral(
        input: Value<*>,
        variables: CoercedVariables,
        graphQLContext: GraphQLContext,
        locale: Locale
    ): LocalDateTime? {
        return if (input is StringValue) {
            try {
                LocalDateTime.parse(input.value, formatter)
            } catch (_: Exception) {
                throw CoercingParseLiteralException("Invalid DateTime format.")
            }
        } else {
            throw CoercingParseLiteralException("Expected a StringValue.")
        }
    }
}