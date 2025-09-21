package com.example.myapplication
import androidx.compose.material3.*
import android.os.Bundle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "calculator"
    ) {
        composable("calculator") {
            TipCalculator(
                onNavigateToFeedback = { navController.navigate("feedback") }
            )
        }
        composable("feedback") {
            TipFeedbackScreen(onBack = { navController.popBackStack() })
        }
    }
}

@Composable
fun TipCalculator(onNavigateToFeedback: () -> Unit) {
    var amount by remember { mutableStateOf("") }
    var tipPercent by remember { mutableStateOf(15f) }
    var showResult by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf("") }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Column(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopStart)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Сума рахунку",
                    color = Color(0xFF6431A3),
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Відсоток чайових: ${tipPercent.toInt()}%",
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Slider(
                value = tipPercent,
                onValueChange = { tipPercent = it },
                valueRange = 0f..30f,
                steps = 29,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                Button(
                    onClick = {
                        val bill = amount.toDoubleOrNull()
                        if (bill != null) {
                            val tip = bill * tipPercent / 100f
                            val total = bill + tip
                            resultMessage = "Чайові: ${"%.2f".format(tip)} грн\n" +
                                    "Загальна сума: ${"%.2f".format(total)} грн\n" +
                                    when {
                                        tipPercent < 10 -> "Скромно"
                                        tipPercent in 10f..15f -> "Нормально"
                                        else -> "Щедро"
                                    }
                            showResult = true
                        } else {
                            resultMessage = "Введіть правильні дані!"
                            showResult = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text("Порахувати")
                }

                if (showResult) {
                    AlertDialog(
                        onDismissRequest = { showResult = false },
                        title = { Text("Результат") },
                        text = { Text(resultMessage) },
                        confirmButton = {
                            TextButton(onClick = { showResult = false }) {
                                Text("OK")
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onNavigateToFeedback,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text("Залишити відгук")
                }
            }
        }
    }
}

@Composable
fun TipFeedbackScreen(onBack: () -> Unit) {
    var comment by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(3f) }
    var showThanks by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Залиш свій відгук", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            label = { Text("Ваш коментар") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Оцініть сервіс: ${rating.toInt()} з 5")
        Slider(
            value = rating,
            onValueChange = { rating = it },
            valueRange = 1f..5f,
            steps = 3,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { showThanks = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Відправити")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Назад")
        }

        if (showThanks) {
            AlertDialog(
                onDismissRequest = { showThanks = false },
                title = { Text("Дякуємо!") },
                text = {
                    Column {
                        Text("Ваш відгук: \"$comment\"")

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Оцінка: ${rating.toInt()} ")
                            repeat(rating.toInt()) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "Star",
                                    tint = Color(0xFFFFC107)
                                )
                            }
                        }
                    }
                }
                ,
                confirmButton = {
                    TextButton(onClick = { showThanks = false }) {
                        Text("OK")
                    }
                }
            )
        }

    }
}