package supervisor.exception

import org.springframework.graphql.execution.ErrorType
import supervisor.exception.annotation.ErrorCodeRange

@ErrorCodeRange(1_100, 1_200)
class ResourceInUseException private constructor(
    code: Int, message: String
) : ResourceException(code, ErrorType.FORBIDDEN, message) {
    companion object {
        val USERNAME =
            ResourceInUseException(1100, "Username already in use.")
        val EMAIL =
            ResourceInUseException(1101, "Email already in use.")
    }
}