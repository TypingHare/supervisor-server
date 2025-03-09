package supervisor.exception.annotation

/**
 * Represents a range of error codes that can be used by the annotated class.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ErrorCodeRange(
    val start: Int,
    val end: Int,
)