package supervisor.common

fun throwIf(condition: Boolean, throwableProvider: () -> Throwable) {
    if (condition) {
        throw throwableProvider()
    }
}

fun throwIfNull(any: Any?, throwableProvider: () -> Throwable) {
    throwIf(any == null, throwableProvider)
}

fun throwIfNotNull(any: Any?, throwableProvider: () -> Throwable) {
    throwIf(any != null, throwableProvider)
}