package dev.forcetower.instrack.core.source

sealed class Operation<T> {
    data class Success<T>(val data: T, val code: Int): Operation<T>()
    data class Loading<T>(val data: T?) : Operation<T>()
    data class Error<T>(val error: Throwable, val code: Int, val data: T?) : Operation<T>()

    companion object {
        fun <T> success(data: T, code: Int): Operation<T> {
            return Success(data, code)
        }

        fun <T> loading(data: T? = null): Operation<T> {
            return Loading(data)
        }

        fun <T> error(error: Throwable, code: Int = 0, data: T? = null): Operation<T> {
            return Error(error, code, data)
        }
    }
}