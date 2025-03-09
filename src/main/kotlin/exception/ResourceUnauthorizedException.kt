package supervisor.exception

import org.springframework.graphql.execution.ErrorType
import supervisor.exception.annotation.ErrorCodeRange

@ErrorCodeRange(13_000, 14_000)
class ResourceUnauthorizedException private constructor(
    code: Int, message: String
) : ResourceException(code, ErrorType.UNAUTHORIZED, message) {
    companion object {
        val PROJECT_UPDATE_NOT_ALLOWED =
            ResourceUnauthorizedException(
                13_000,
                "You are not authorized to update this project."
            )
        val PROJECT_DELETE_NOT_ALLOWED =
            ResourceUnauthorizedException(
                13_001,
                "You are not authorized to delete this project."
            )
        val ENTRY_UPDATE_NOT_ALLOWED = ResourceUnauthorizedException(
            13_002,
            "You are not authorized to update this entry."
        )
        val ENTRY_DELETE_NOT_ALLOWED = ResourceUnauthorizedException(
            13_003,
            "You are not authorized to delete this entry."
        )
    }
}