package com.github.flocky

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BankFormViewModel @Inject constructor() : ViewModel() {

    private var _uiState = mutableStateOf(UI())
    val uiState: State<UI> = _uiState

    val validationEvent = MutableSharedFlow<UIEvent.ValidationEvent>()

    /**
     * This method will trigger on user interactions and the impact will be state change of the ui.
     */
    fun onEvent(event: UIEvent) {
        when (event) {
            is UIEvent.AccountNumberChanged -> {
                val accountNumber =
                    _uiState.value.accountNumber.copy(accountNumber = event.accountNumber)
                _uiState.value = _uiState.value.copy(accountNumber = accountNumber)
            }
            is UIEvent.PANNumberChanged -> {
                val panNumber = _uiState.value.panNumber.copy(panNumber = event.panNumber)
                _uiState.value = _uiState.value.copy(panNumber = panNumber)
            }

            is UIEvent.AccountNameChanged -> {
                val accountName = _uiState.value.accountName.copy(accountName = event.accountName)
                _uiState.value = _uiState.value.copy(accountName = accountName)
            }
            is UIEvent.Submit -> {
                validateInputs()
            }
        }
    }

    private fun validateInputs() {
        val accountNumberResult = Validator.validateAccountNumber(_uiState.value.accountNumber)
        val accountNumber =
            _uiState.value.accountNumber.copy(hasAccountNumberValidationError = !accountNumberResult.status)
        _uiState.value = _uiState.value.copy(accountNumber = accountNumber)

        val panResult = Validator.validatePAN(_uiState.value.panNumber)

        val panNumber =
            _uiState.value.panNumber.copy(hasPanNumberValidationError = !panResult.status)
        _uiState.value = _uiState.value.copy(panNumber = panNumber)

        val accountNameResult = Validator.validateOwnerName(_uiState.value.accountName)

        val accountName =
            _uiState.value.accountName.copy(hasAccountNameValidationError = !accountNameResult.status)
        _uiState.value = _uiState.value.copy(accountName = accountName)

        val hasError = listOf(
            accountNumberResult,
            panResult,
            accountNameResult
        ).any { !it.status }
        viewModelScope.launch {
            if (!hasError) {
                validationEvent.emit(UIEvent.ValidationEvent.Success(msg = SuccessMessageValidation))
            } else {
                validationEvent.emit(UIEvent.ValidationEvent.Failure(msg = ErrorMessageValidation))
            }
        }
    }
}
