package supervisor.exception

import org.springframework.graphql.execution.ErrorType
import supervisor.exception.annotation.ErrorCodeRange

@ErrorCodeRange(3_000, 4_000)
class InvalidOperationException private constructor(
    code: Int, message: String
) : ServiceException(code, ErrorType.FORBIDDEN, message) {
    companion object {
        val START_STARTED_ENTRY = InvalidOperationException(
            3000,
            "You cannot start an entry that has been started."
        )
        val STOP_NOT_STARTED_ENTRY = InvalidOperationException(
            3001,
            "You cannot stop the entry that has not been started."
        )
        val STOP_STOPPED_ENTRY = InvalidOperationException(
            3002,
            "You cannot stop the entry that has been stopped."
        )
        val CREATE_ENTRY_WHEN_ONE_ALREADY_EXISTS = InvalidOperationException(
            3003,
            "You cannot create an entry when another non-stop entry exists."
        )
    }
}