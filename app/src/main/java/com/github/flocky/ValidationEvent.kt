package com.github.flocky

sealed class ValidationEvent {
    class Success(val msg: String) : ValidationEvent()
    class Failure(val code: Int = 0, val msg: String) : ValidationEvent()
}
