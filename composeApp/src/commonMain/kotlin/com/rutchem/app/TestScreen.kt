package com.rutchem.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TestScreen(
    category: QuizCategory,
    questionCount: Int,
    language: AppLanguage,
    onTestFinished: (Int, Int) -> Unit,
    onBack: () -> Unit
) {
    // Generujemy listę pytań
    val questions = remember {
        List(questionCount) { generateQuestion(category, language) }
    }

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var userAnswer by remember { mutableStateOf("") }

    val currentQuestion = questions[currentQuestionIndex]
    val correctAnswerString = currentQuestion.options[currentQuestion.correctIndex]

    val progress = (currentQuestionIndex + 1).toFloat() / questionCount.toFloat()

    // Teksty (wyciągnięte do zmiennych dla czytelności i uniknięcia błędów składni)
    val labelQuestion = if (language == AppLanguage.PL) "Pytanie" else "Question"
    val labelType = if (language == AppLanguage.PL) "Wpisz odpowiedź:" else "Type your answer:"
    val labelNext = if (language == AppLanguage.PL) "Dalej" else "Next"
    val labelFinish = if (language == AppLanguage.PL) "Zakończ" else "Finish"
    val labelInput = if (language == AppLanguage.PL) "Twoja odpowiedź" else "Your answer"
    val labelTitle = if (language == AppLanguage.PL) "Test Wiedzy" else "Knowledge Test"

    val keyboardController = LocalSoftwareKeyboardController.current

    fun submitAnswer() {
        val cleanUser = userAnswer.trim()
        val cleanCorrect = correctAnswerString.trim()

        val isCorrect = if (cleanUser.equals(cleanCorrect, ignoreCase = true)) {
            true
        } else if (cleanCorrect.endsWith("u") && cleanUser.equals(cleanCorrect.replace(" u", ""), ignoreCase = true)) {
            true
        } else {
            false
        }

        if (isCorrect) score++

        userAnswer = ""
        keyboardController?.hide()

        if (currentQuestionIndex < questionCount - 1) {
            currentQuestionIndex++
        } else {
            onTestFinished(score, questionCount)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Górny pasek z przyciskiem powrotu
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = labelTitle,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(8.dp),
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "$labelQuestion ${currentQuestionIndex + 1} / $questionCount",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text(
                text = currentQuestion.text,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(24.dp).fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = labelType,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = userAnswer,
            onValueChange = { userAnswer = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text(labelInput) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { submitAnswer() })
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { submitAnswer() },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (currentQuestionIndex < questionCount - 1) labelNext else labelFinish,
                fontSize = 18.sp
            )
        }
    }
}