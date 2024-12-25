package supervisor.exception.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ErrorCodeRange(
    val start: Int,
    val end: Int,
)