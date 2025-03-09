package supervisor.exception

import org.springframework.graphql.execution.ErrorType
import supervisor.exception.annotation.ErrorCodeRange

@ErrorCodeRange(12_000, 13_000)
class ResourceAlreadyExistException private constructor(
    code: Int, message: String
) : ResourceException(code, ErrorType.FORBIDDEN, message) {
    companion object {
        val PROJECT =
            ResourceAlreadyExistException(
                12_000,
                "Project with such name already exists"
            )
    }
}