package com.rutchem.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
    onTestFinished: (Int, Int) -> Unit
) {
    // Generujemy listę pytań
    val questions = remember {
        List(questionCount) { generateQuestion(category, language) }
    }

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }

    // Zmienna na to, co wpisuje użytkownik
    var userAnswer by remember { mutableStateOf("") }

    val currentQuestion = questions[currentQuestionIndex]

    // Wyciągamy poprawną odpowiedź z opcji (wiemy która jest poprawna dzięki correctIndex)
    val correctAnswerString = currentQuestion.options[currentQuestion.correctIndex]

    val progress = (currentQuestionIndex + 1).toFloat() / questionCount.toFloat()
    val qLabel = if (language == AppLanguage.PL) "Pytanie" else "Question"
    val typeLabel = if (language == AppLanguage.PL) "Wpisz odpowiedź:" else "Type your answer:"
    val nextLabel = if (language == AppLanguage.PL) "Dalej" else "Next"
    val finishLabel = if (language == AppLanguage.PL) "Zakończ" else "Finish"

    // Kontroler klawiatury (żeby ją chować po kliknięciu)
    val keyboardController = LocalSoftwareKeyboardController.current

    // Funkcja sprawdzająca i przechodząca dalej (użyjemy jej w przycisku i w klawiaturze)
    fun submitAnswer() {
        // --- LOGIKA SPRAWDZANIA ---
        // 1. Usuwamy spacje z początku/końca (trim)
        // 2. Ignorujemy wielkość liter (equals ignoreCase)
        // 3. Dla ułatwienia przy masie: usuwamy " u" jeśli użytkownik go nie wpisał

        val cleanUser = userAnswer.trim()
        val cleanCorrect = correctAnswerString.trim()

        // Specjalne sprawdzenie dla liczb/masy (zeby "1.008" zaliczyło jako "1.008 u")
        val isCorrect = if (cleanUser.equals(cleanCorrect, ignoreCase = true)) {
            true
        } else if (cleanCorrect.endsWith("u") && cleanUser.equals(cleanCorrect.replace(" u", ""), ignoreCase = true)) {
            true // Zalicza, jeśli użytkownik wpisał samą liczbę bez jednostki "u"
        } else {
            false
        }

        if (isCorrect) {
            score++
        }

        // Resetujemy pole tekstowe
        userAnswer = ""
        keyboardController?.hide()

        // Przechodzimy dalej
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
        // Pasek postępu
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(8.dp),
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "$qLabel ${currentQuestionIndex + 1} / $questionCount",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Treść pytania
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

        // --- POLE DO WPISYWANIA ---
        Text(typeLabel, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = userAnswer,
            onValueChange = { userAnswer = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text(if(language==AppLanguage.PL) "Twoja odpowiedź" else "Your answer") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { submitAnswer() } // Enter na klawiaturze zatwierdza
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Przycisk Dalej
        Button(
            onClick = { submitAnswer() },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (currentQuestionIndex < questionCount - 1) nextLabel else finishLabel,
                fontSize = 18.sp
            )
        }
    }
}