package supervisor.exception

import org.springframework.graphql.execution.ErrorType
import supervisor.exception.annotation.ErrorCodeRange

@ErrorCodeRange(10_000, 11_000)
class ResourceNotFoundException private constructor(
    code: Int, message: String
) : ResourceException(code, ErrorType.NOT_FOUND, message) {
    companion object {
        val USER = ResourceNotFoundException(10_000, "User not found.")
        val PROJECT = ResourceNotFoundException(10_001, "Project not found.")
        val ENTRY = ResourceNotFoundException(10_002, "Entry not found.")
    }
}