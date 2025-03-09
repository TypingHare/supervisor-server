package supervisor.exception

import org.springframework.graphql.execution.ErrorType
import supervisor.exception.annotation.ErrorCodeRange

@ErrorCodeRange(10_000, 20_000)
open class ResourceException protected constructor(
    code: Int,
    type: ErrorType,
    message: String
) : ServiceException(code, type, message)