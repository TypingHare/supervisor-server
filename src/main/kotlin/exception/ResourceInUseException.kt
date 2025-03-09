package supervisor.exception

import org.springframework.graphql.execution.ErrorType
import supervisor.exception.annotation.ErrorCodeRange

@ErrorCodeRange(11_000, 12_000)
class ResourceInUseException private constructor(
    code: Int, message: String
) : ResourceException(code, ErrorType.FORBIDDEN, message) {
    companion object {
        val USERNAME =
            ResourceInUseException(11_000, "Username already in use.")
        val EMAIL =
            ResourceInUseException(11_001, "Email already in use.")
    }
}