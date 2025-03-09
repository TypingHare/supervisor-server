package supervisor.graphql

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

/**
 * Custom GraphQL scalar type for handling LocalDateTime values.
 *
 * This class implements conversion between Java LocalDateTime objects and
 * ISO-formatted date-time strings in GraphQL operations. It enables
 * serialization of LocalDateTime objects to ISO 8601 formatted strings, and
 * deserialization of ISO 8601 strings back to LocalDateTime objects.
 *
 * The scalar uses ISO_DATE_TIME formatter for consistent date-time string
 * representation across all GraphQL operations including variables, literals,
 * and results.
 *
 * @see supervisor.config.GraphQLConfiguration
 */
class DateTimeScalar : Coercing<LocalDateTime, String> {
    private val formatter = DateTimeFormatter.ISO_DATE_TIME

    override fun serialize(
        dataFetcherResult: Any,
        graphQLContext: GraphQLContext,
        locale: Locale
    ): String {
        if (dataFetcherResult !is LocalDateTime) {
            throw CoercingSerializeException("Expected a LocalDateTime object.")
        }

        return dataFetcherResult.format(formatter)
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
        if (input !is StringValue) {
            throw CoercingParseLiteralException("Expected a StringValue.")
        }

        return parseValue(input.value, graphQLContext, locale)
    }
}