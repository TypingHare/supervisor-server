package supervisor.exception

import org.springframework.graphql.execution.ErrorType
import supervisor.exception.annotation.ErrorCodeRange

@ErrorCodeRange(0, Int.MAX_VALUE)
abstract class ServiceException protected constructor(
    val code: Int,
    val type: ErrorType,
    message: String,
) : RuntimeException(message)