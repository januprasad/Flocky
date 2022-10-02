package com.github.flocky

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.flocky.ui.theme.FlockyTheme
import kotlinx.coroutines.flow.collectLatest

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
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bank Details") },
                backgroundColor = MaterialTheme.colors.primary
            )
        },
        scaffoldState = scaffoldState,
        content = {
            UIScreen(scaffoldState)
        }
    )
}

@Composable
fun UIScreen(scaffoldState: ScaffoldState) {
    val bankFormViewModel = viewModel(modelClass = BankFormViewModel::class.java)
    val state by bankFormViewModel.uiState
    var checkedState by remember { mutableStateOf(false) }
    val localFocus = LocalFocusManager.current
    LaunchedEffect(key1 = scaffoldState) {
        bankFormViewModel.validationEvent.collect { event ->
            when (event) {
                is UIEvent.ValidationEvent.Success -> {
                    showSnackWrapper(scaffoldState, event.msg)
                }
                is UIEvent.ValidationEvent.Failure -> {
                    showSnackWrapper(scaffoldState, event.msg)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InfoCard()
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BankInputField(
                    state.accountNumber,
                    onTextChanged = {
                        bankFormViewModel.onEvent(UIEvent.AccountNumberChanged(it))
                    },
                    isError = state.accountNumber.hasAccountNumberValidationError,
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
                    isError = state.accountName.hasAccountNameValidationError,
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
                    isError = state.panNumber.hasPanNumberValidationError,
                    onNext = {
                        localFocus.moveFocus(FocusDirection.Down)
                    },
                    onDone = {
                    }
                )
            }
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
                    .wrapContentWidth(Alignment.End)
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(2.dp)) {
                    Checkbox(
                        checked = checkedState,
                        onCheckedChange = { checkedState = it }
                    )
                    Text(text = "I accept these are legitimate values")
                }

                Button(
                    enabled = checkedState,
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
//        Spacer(modifier = Modifier.weight(1f))
    }
}

suspend fun showSnackWrapper(scaffoldState: ScaffoldState, msg: String) {
    scaffoldState.snackbarHostState.showSnackbar(
        msg,
        "Dismiss Me!"
    )
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

@Composable
fun DefaultSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = { }
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                content = {
                    Text(
                        text = data.message,
                        style = MaterialTheme.typography.body2
                    )
                },
                action = {
                    data.actionLabel?.let { actionLabel ->
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = actionLabel,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2
                            )
                        }
                    }
                }
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Bottom)
    )
}

@Composable
@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 720, heightDp = 360)
fun AppPreview() {
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
