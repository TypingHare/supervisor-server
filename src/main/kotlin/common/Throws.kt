package supervisor.common

/**
 * Throws an exception if the condition is satisfied.
 */
fun throwIf(condition: Boolean, throwableProvider: () -> Throwable) {
    if (condition) {
        throw throwableProvider()
    }
}

/**
 * Throws an exception if the object provided is null.
 */
fun throwIfNull(any: Any?, throwableProvider: () -> Throwable) {
    throwIf(any == null, throwableProvider)
}

/**
 * Throws an exception if the object provided is not null.
 */
fun throwIfNotNull(any: Any?, throwableProvider: () -> Throwable) {
    throwIf(any != null, throwableProvider)
}