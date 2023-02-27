package com.github.flocky

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    val c = LocalContext.current
    LaunchedEffect(Unit) {
        Toast.makeText(c, "Welcome!", Toast.LENGTH_LONG).show()
    }

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
    LaunchedEffect(true) {
        showSnackWrapper(scaffoldState, "Hello!")
    }
    val bankFormViewModel = viewModel(modelClass = BankFormViewModel::class.java)

    val accountName by remember {
        mutableStateOf(bankFormViewModel.uiState.value.accountName)
    }

    val accountNumber by remember {
        mutableStateOf(bankFormViewModel.uiState.value.accountNumber)
    }

    val panNumber by remember {
        mutableStateOf(bankFormViewModel.uiState.value.panNumber)
    }

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
                AddressUIComponent(
                    accountNumber,
                    onTextChanged = {
                        bankFormViewModel.onEvent(UIEvent.AccountNumberChanged(it))
                    },
                    isError = accountNumber.hasAccountNumberValidationError,
                    onNext = {
                        localFocus.moveFocus(FocusDirection.Down)
                    },
                    onDone = {
                    }
                )
                AddressUIComponent(
                    accountName,
                    onTextChanged = {
                        bankFormViewModel.onEvent(UIEvent.AccountNameChanged(it))
                    },
                    isError = accountName.hasAccountNameValidationError,
                    onNext = {
                        localFocus.moveFocus(FocusDirection.Down)
                    },
                    onDone = {
                    }
                )
                AddressUIComponent(
                    panNumber,
                    onTextChanged = {
                        bankFormViewModel.onEvent(UIEvent.PANNumberChanged(it))
                    },
                    isError = panNumber.hasPanNumberValidationError,
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                ) {
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
