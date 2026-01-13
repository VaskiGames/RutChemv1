package com.rutchem.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class QuizCategory { SYMBOL, MASS, NUMBER, ALL }

data class QuizQuestion(val text: String, val options: List<String>, val correctIndex: Int)

@Composable
fun QuizScreen(category: QuizCategory, language: AppLanguage) { // <--- Odbieramy język
    var score by remember { mutableStateOf(0) }
    // Przekazujemy język do generatora
    var currentQuestion by remember { mutableStateOf(generateQuestion(category, language)) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var isAnswerCorrect by remember { mutableStateOf(false) }

    val scoreText = if (language == AppLanguage.PL) "Wynik: $score" else "Score: $score"
    val nextText = if (language == AppLanguage.PL) "Następne pytanie" else "Next Question"

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(scoreText, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(40.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text(
                text = currentQuestion.text,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(24.dp).fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        currentQuestion.options.forEachIndexed { index, option ->
            val buttonColor = when {
                selectedOption != null && index == currentQuestion.correctIndex -> Color(0xFF4CAF50)
                selectedOption == index && !isAnswerCorrect -> Color(0xFFE53935)
                else -> MaterialTheme.colorScheme.primaryContainer
            }
            Button(
                onClick = {
                    if (selectedOption == null) {
                        selectedOption = index
                        isAnswerCorrect = index == currentQuestion.correctIndex
                        if (isAnswerCorrect) score++
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).height(55.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = option, fontSize = 18.sp, color = Color.Black)
            }
        }
        Spacer(modifier = Modifier.height(30.dp))

        if (selectedOption != null) {
            Button(
                onClick = {
                    currentQuestion = generateQuestion(category, language)
                    selectedOption = null
                    isAnswerCorrect = false
                },
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text(nextText)
            }
        }
    }
}

fun generateQuestion(category: QuizCategory, language: AppLanguage): QuizQuestion {
    val correctElement = periodicTableData.random()
    val distractors = periodicTableData.filter { it != correctElement }.shuffled().take(3)

    // Pobieramy nazwę w odpowiednim języku
    val elName = correctElement.getName(language)

    val questionType = when (category) {
        QuizCategory.MASS -> 0
        QuizCategory.SYMBOL -> 1
        QuizCategory.NUMBER -> 2
        QuizCategory.ALL -> (0..2).random()
    }

    val questionText: String
    val correctAnswer: String
    val wrongAnswers: List<String>

    if (language == AppLanguage.PL) {
        // --- POLSKI ---
        when (questionType) {
            0 -> {
                questionText = "Jaka jest masa atomowa pierwiastka: $elName?"
                correctAnswer = "${correctElement.mass} u"
                wrongAnswers = distractors.map { "${it.mass} u" }
            }
            1 -> {
                questionText = "Jaki symbol chemiczny ma $elName?"
                correctAnswer = correctElement.symbol
                wrongAnswers = distractors.map { it.symbol }
            }
            else -> {
                questionText = "Jaka jest liczba atomowa pierwiastka: $elName?"
                correctAnswer = "${correctElement.number}"
                wrongAnswers = distractors.map { "${it.number}" }
            }
        }
    } else {
        // --- ANGIELSKI ---
        when (questionType) {
            0 -> {
                questionText = "What is the atomic mass of: $elName?"
                correctAnswer = "${correctElement.mass} u"
                wrongAnswers = distractors.map { "${it.mass} u" }
            }
            1 -> {
                questionText = "What is the symbol of: $elName?"
                correctAnswer = correctElement.symbol
                wrongAnswers = distractors.map { it.symbol }
            }
            else -> {
                questionText = "What is the atomic number of: $elName?"
                correctAnswer = "${correctElement.number}"
                wrongAnswers = distractors.map { "${it.number}" }
            }
        }
    }

    val allOptions = (wrongAnswers + correctAnswer).shuffled()
    val correctIndex = allOptions.indexOf(correctAnswer)
    return QuizQuestion(questionText, allOptions, correctIndex)
}