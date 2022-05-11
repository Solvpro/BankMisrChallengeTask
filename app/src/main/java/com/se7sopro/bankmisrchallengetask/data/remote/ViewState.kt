package com.se7sopro.bankmisrchallengetask.data.remote


sealed class ViewState<T>(
    val data: T? = null,
    val errorCode: Int? = null
) {
    class Success<T>(data: T) : ViewState<T>(data)
    class Loading<T>(val isVisible: Boolean) : ViewState<T>()
    class DataError<T>(errorCode: Int,errorData : T) : ViewState<T>(errorData, errorCode)
    class GeneralError<T>(errorCode: Int,errorData : T) : ViewState<T>(errorData, errorCode)


    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is DataError -> "Error[exception=$errorCode]"
            is GeneralError -> "Error[exception=$errorCode]"
            is Loading<T> -> "Loading"
        }
    }

}