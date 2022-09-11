package com.github.flocky

sealed class UIEvent {
    data class AccountNumberChanged(val accountNumber: String) : UIEvent()
    data class PANNumberChanged(val panNumber: String) : UIEvent()
    data class AccountNameChanged(val accountName: String) : UIEvent()
    object Submit : UIEvent()
}
