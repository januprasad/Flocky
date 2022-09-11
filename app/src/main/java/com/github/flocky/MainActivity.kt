package com.github.flocky

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.flocky.ui.theme.FlockyTheme
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardCapitalization

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlockyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bank Details") },
                backgroundColor = MaterialTheme.colors.primary
            )
        },
        content = {
            Screen(it)
        }
    )
}

@Composable
fun Screen(paddingValues: PaddingValues) {
    val bankFormViewModel = viewModel(modelClass = BankFormViewModel::class.java)
    val state by bankFormViewModel.uiState
    val localFocus = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        bankFormViewModel.validationEvent.collectLatest { event ->
            when (event) {
                is ValidationEvent.Success -> {
                    Toast
                        .makeText(context, "All inputs are valid", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InfoCard()
        BankInputField(
            state.accountNumber,
            onTextChanged = {
                bankFormViewModel.onEvent(UIEvent.AccountNumberChanged(it))
            },
            isError = state.hasAccountNumberError,
            onNext = {
                localFocus.moveFocus(FocusDirection.Down)
            },
            onDone = {
            }
        )
        BankInputField(
            state.accountName,
            onTextChanged = {
                bankFormViewModel.onEvent(UIEvent.AccountNameChanged(it))
            },
            isError = state.hasNameError,
            onNext = {
                localFocus.moveFocus(FocusDirection.Down)
            },
            onDone = {
            }
        )
        BankInputField(
            state.panNumber,
            onTextChanged = {
                bankFormViewModel.onEvent(UIEvent.PANNumberChanged(it))
            },
            isError = state.hasPANError,
            onNext = {
                localFocus.moveFocus(FocusDirection.Down)
            },
            onDone = {
            }
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .padding(0.dp, 8.dp)
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                onClick = {
                    bankFormViewModel.onEvent(UIEvent.Submit)
                }
            ) {
                Text("Submit")
            }
        }
    }
}

@Composable
fun InfoCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.W900, color = Color(0xFF4552B8))
                    ) {
                        append("HDFC ")
                    }
                }
            )
            Text(
                buildAnnotatedString {
                    append("One Click ")
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.W900, color = Color(0xFFC00A48))
                    ) {
                        append("Apply Loan")
                    }
                }
            )
        }
    }
}
