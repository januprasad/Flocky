package com.github.flocky

sealed class ValidationEvent {
    object Success : ValidationEvent()
}
