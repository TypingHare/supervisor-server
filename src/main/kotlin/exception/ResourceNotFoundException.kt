package supervisor.exception

import org.springframework.graphql.execution.ErrorType
import supervisor.exception.annotation.ErrorCodeRange

@ErrorCodeRange(1_000, 1_100)
class ResourceNotFoundException private constructor(
    code: Int, message: String
) : ResourceException(code, ErrorType.NOT_FOUND, message) {
    companion object {
        val USER = ResourceNotFoundException(1000, "User not found.")
        val PROJECT = ResourceNotFoundException(1001, "Project not found.")
        val ENTRY = ResourceNotFoundException(1002, "Entry not found.")
    }
}