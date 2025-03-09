package supervisor.interceptor

import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.graphql.execution.ErrorType
import org.springframework.stereotype.Component
import supervisor.exception.ServiceException

/**
 * Intercepts and handles exceptions thrown during GraphQL data fetching
 * operations.
 *
 * This interceptor converts Java exceptions into structured GraphQL errors with
 * additional metadata, providing clients with consistent error responses. It
 * specifically handles {@link ServiceException} instances with their custom
 * error codes and types, while applying default error information for other
 * exception types.
 *
 * When an exception occurs during GraphQL execution, this interceptor formats
 * it into a standardized error response containing:
 * - A human-readable error message
 * - The GraphQL path where the error occurred
 * - The location within the GraphQL document
 * - Extensions with error code and type information
 *
 * @see DataFetcherExceptionResolverAdapter
 * @see ServiceException
 * @see GraphQLError
 */
@Component
class ExceptionInterceptor : DataFetcherExceptionResolverAdapter() {
    override fun resolveToSingleError(
        ex: Throwable,
        env: DataFetchingEnvironment
    ): GraphQLError {
        val extensions = when (ex) {
            is ServiceException -> mapOf(
                "errorCode" to ex.code,
                "errorType" to ex.type
            )
            else -> mapOf(
                "errorCode" to DEFAULT_ERROR_CODE,
                "errorType" to DEFAULT_ERROR_TYPE
            )
        }

        return GraphqlErrorBuilder.newError()
            .message(ex.message ?: DEFAULT_ERROR_MESSAGE)
            .path(env.executionStepInfo.path)
            .location(env.field.sourceLocation)
            .extensions(extensions)
            .build()
    }

    companion object {
        private const val DEFAULT_ERROR_MESSAGE = "Unknown error"
        private const val DEFAULT_ERROR_CODE = -1
        private val DEFAULT_ERROR_TYPE = ErrorType.INTERNAL_ERROR
    }
}