package com.github.flocky

object Validator {

    fun validateAccountNumber(accountNumber: AccountNumber): ValidationResult {
        return ValidationResult(
            accountNumber.value.isNotEmpty() &&
                accountNumber.value.length == accountNumber.length
        )
    }

    fun validatePAN(pan: PANNumber): ValidationResult {
        return ValidationResult(pan.value.isNotEmpty() && pan.value.length == pan.length)
    }

    fun validateOwnerName(name: AccountName): ValidationResult {
        return ValidationResult(name.value.isNotEmpty() && name.value.length < name.length)
    }
}

data class ValidationResult(
    val status: Boolean = false
)
