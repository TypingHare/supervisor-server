package supervisor.interceptor

import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.graphql.execution.ErrorType
import org.springframework.stereotype.Component
import supervisor.exception.ServiceException

@Component
class ExceptionInterceptor : DataFetcherExceptionResolverAdapter() {
    override fun resolveToSingleError(
        ex: Throwable,
        env: DataFetchingEnvironment
    ): GraphQLError {
        return GraphqlErrorBuilder.newError()
            .message(ex.message ?: DEFAULT_ERROR_MESSAGE)
            .path(env.executionStepInfo.path)
            .location(env.field.sourceLocation)
            .extensions(getExtensions(ex))
            .build()
    }

    private fun getExtensions(ex: Throwable): Map<String, Any> {
        return mapOf(
            "errorCode" to when (ex) {
                is ServiceException -> ex.code
                else -> DEFAULT_ERROR_CODE
            },
            "errorType" to when (ex) {
                is ServiceException -> ex.type
                else -> DEFAULT_ERROR_TYPE
            }
        )
    }

    companion object {
        const val DEFAULT_ERROR_MESSAGE = "An unexpected error occurred"
        const val DEFAULT_ERROR_CODE = -1
        val DEFAULT_ERROR_TYPE = ErrorType.INTERNAL_ERROR
    }
}