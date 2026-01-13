package com.rutchem.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TestResultScreen(
    score: Int,
    total: Int,
    language: AppLanguage,
    onBackToMenu: () -> Unit
) {
    val percentage = (score.toDouble() / total.toDouble() * 100).toInt()

    // Tłumaczenia
    val header = if (language == AppLanguage.PL) "Koniec Testu!" else "Test Finished!"
    val scoreText = if (language == AppLanguage.PL) "Twój wynik:" else "Your Score:"
    val backBtn = if (language == AppLanguage.PL) "Wróć do Menu" else "Back to Menu"

    // Komentarz zależny od wyniku
    val comment = if (language == AppLanguage.PL) {
        when {
            percentage == 100 -> "Perfekcyjnie! Mistrz Chemii! 🏆"
            percentage >= 80 -> "Świetna robota! 🧪"
            percentage >= 50 -> "Całkiem nieźle!"
            else -> "Musisz jeszcze poćwiczyć."
        }
    } else {
        when {
            percentage == 100 -> "Perfect! Chemistry Master! 🏆"
            percentage >= 80 -> "Great job! 🧪"
            percentage >= 50 -> "Not bad!"
            else -> "Keep practicing."
        }
    }

    val color = if (percentage >= 50) Color(0xFF4CAF50) else Color(0xFFE53935)

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(header, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(32.dp))

        Text(scoreText, style = MaterialTheme.typography.titleLarge)
        Text(
            "$score / $total",
            fontSize = 64.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            "$percentage%",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(comment, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(64.dp))

        Button(
            onClick = onBackToMenu,
            modifier = Modifier.fillMaxWidth(0.7f).height(50.dp)
        ) {
            Text(backBtn)
        }
    }
}