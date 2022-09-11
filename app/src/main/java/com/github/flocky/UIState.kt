package com.github.flocky

import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType

data class UIState(
    val accountNumber: AccountNumber = AccountNumber(),
    val panNumber: PANNumber = PANNumber(),
    val accountName: AccountName = AccountName(),
    val hasAccountNumberError: Boolean = false,
    val hasNameError: Boolean = false,
    val hasPANError: Boolean = false
)

sealed class ComponentProps(
    var value: String = "",
    val label: String = "",
    val length: Int = 0,
    open val keyboardType: KeyboardType,
    val keyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.None
)

data class AccountNumber(
    val accountNumber: String = "",
    val accountNumberLabel: String = AccountNumberLabel,
    val accountNumberLength: Int = 10,
    val accountNumberKeyboardType: KeyboardType = KeyboardType.Number
) : ComponentProps(
    accountNumber,
    accountNumberLabel,
    accountNumberLength,
    accountNumberKeyboardType
)

data class AccountName(
    val accountName: String = "",
    val accountNameLabel: String = AccountNameLabel,
    val accountNameLength: Int = 20,
    val accountNameKeyboardType: KeyboardType = KeyboardType.Text,
    val accountNameKeyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences
) : ComponentProps(
    accountName,
    accountNameLabel,
    accountNameLength,
    accountNameKeyboardType,
    accountNameKeyboardCapitalization
)

data class PANNumber(
    val panNumber: String = "",
    val panNumberLabel: String = PANLabel,
    val panNumberLength: Int = 10,
    val panNumberKeyboardType: KeyboardType = KeyboardType.Text,
    val panNumberKeyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.Characters
) : ComponentProps(
    panNumber,
    panNumberLabel,
    panNumberLength,
    panNumberKeyboardType,
    panNumberKeyboardCapitalization
)
