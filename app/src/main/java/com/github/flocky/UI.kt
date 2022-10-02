package com.github.flocky

import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType

data class UI(
    val accountNumber: AccountNumber = AccountNumber(),
    val panNumber: PANNumber = PANNumber(),
    val accountName: AccountName = AccountName()
)

sealed class ComponentProps(
    var value: String = "",
    val label: String = "",
    val length: Int = 0,
    val keyboardType: KeyboardType,
    val keyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    val hasValidationError: Boolean = false
)

data class AccountNumber(
    val accountNumber: String = "",
    val accountNumberLabel: String = AccountNumberLabel,
    val accountNumberLength: Int = 10,
    val accountNumberKeyboardType: KeyboardType = KeyboardType.Number,
    val hasAccountNumberValidationError: Boolean = false
) : ComponentProps(
    accountNumber,
    accountNumberLabel,
    accountNumberLength,
    accountNumberKeyboardType,
    hasValidationError = hasAccountNumberValidationError
)

data class AccountName(
    val accountName: String = "",
    val accountNameLabel: String = AccountNameLabel,
    val accountNameLength: Int = 20,
    val accountNameKeyboardType: KeyboardType = KeyboardType.Text,
    val accountNameKeyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    val hasAccountNameValidationError: Boolean = false
) : ComponentProps(
    accountName,
    accountNameLabel,
    accountNameLength,
    accountNameKeyboardType,
    accountNameKeyboardCapitalization,
    hasValidationError = hasAccountNameValidationError
)

data class PANNumber(
    val panNumber: String = "",
    val panNumberLabel: String = PANLabel,
    val panNumberLength: Int = 10,
    val panNumberKeyboardType: KeyboardType = KeyboardType.Text,
    val panNumberKeyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.Characters,
    val hasPanNumberValidationError: Boolean = false
) : ComponentProps(
    panNumber,
    panNumberLabel,
    panNumberLength,
    panNumberKeyboardType,
    panNumberKeyboardCapitalization,
    hasValidationError = hasPanNumberValidationError
)
