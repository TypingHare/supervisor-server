package supervisor.exception

import org.springframework.graphql.execution.ErrorType
import supervisor.exception.annotation.ErrorCodeRange

@ErrorCodeRange(1_200, 1_300)
class ResourceAlreadyExistException private constructor(
    code: Int, message: String
) : ResourceException(code, ErrorType.FORBIDDEN, message) {
    companion object {
        val PROJECT =
            ResourceAlreadyExistException(
                1200,
                "Project with such name already exists"
            )
    }
}