package supervisor.exception

import org.springframework.graphql.execution.ErrorType
import supervisor.exception.annotation.ErrorCodeRange

@ErrorCodeRange(2_000, 3_000)
class ValidationException private constructor(
    code: Int, message: String
) : ServiceException(code, ErrorType.FORBIDDEN, message) {
    companion object {
        val USER_PASSWORD =
            ValidationException(2000, "User password is incorrect.")
        val USER_TOKEN_MISSING =
            ValidationException(2001, "User token is missing.")
        val USER_TOKEN_INVALID =
            ValidationException(2002, "User token is invalid.")
    }
}