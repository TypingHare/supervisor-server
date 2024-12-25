package supervisor.exception

import org.springframework.graphql.execution.ErrorType
import supervisor.exception.annotation.ErrorCodeRange

@ErrorCodeRange(1_300, 1_400)
class ResourceUnauthorizedException private constructor(
    code: Int, message: String
) : ResourceException(code, ErrorType.UNAUTHORIZED, message) {
    companion object {
        val PROJECT_UPDATE_NOT_ALLOWED =
            ResourceUnauthorizedException(
                1300,
                "You are not authorized to update this project."
            )
        val PROJECT_DELETE_NOT_ALLOWED =
            ResourceUnauthorizedException(
                1301,
                "You are not authorized to delete this project."
            )
        val ENTRY_UPDATE_NOT_ALLOWED = ResourceUnauthorizedException(
            1302,
            "You are not authorized to update this entry."
        )
        val ENTRY_DELETE_NOT_ALLOWED = ResourceUnauthorizedException(
            1303,
            "You are not authorized to delete this entry."
        )
    }
}