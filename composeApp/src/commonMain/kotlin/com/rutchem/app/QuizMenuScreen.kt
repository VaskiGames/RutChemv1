package com.rutchem.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuizMenuScreen(
    language: AppLanguage,
    onCategorySelected: (QuizCategory) -> Unit,
    onBack: () -> Unit // <--- DODANO: Parametr wymagany przez App.kt
) {
    val title = if (language == AppLanguage.PL) "Wybierz kategorię:" else "Select category:"

    val txtSymbol = if (language == AppLanguage.PL) "Symbol" else "Symbol"
    val txtMass = if (language == AppLanguage.PL) "Masa" else "Mass"
    val txtNumber = if (language == AppLanguage.PL) "Liczba At." else "Atomic Num."
    val txtAll = if (language == AppLanguage.PL) "Wszystko" else "All"
    val txtBack = if (language == AppLanguage.PL) "Wróć" else "Back"

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(title, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 32.dp))

        QuizMenuButton(txtSymbol) { onCategorySelected(QuizCategory.SYMBOL) }
        Spacer(modifier = Modifier.height(12.dp))
        QuizMenuButton(txtMass) { onCategorySelected(QuizCategory.MASS) }
        Spacer(modifier = Modifier.height(12.dp))
        QuizMenuButton(txtNumber) { onCategorySelected(QuizCategory.NUMBER) }
        Spacer(modifier = Modifier.height(12.dp))
        QuizMenuButton(txtAll) { onCategorySelected(QuizCategory.ALL) }

        Spacer(modifier = Modifier.height(48.dp))

        // Przycisk Wróć (użycie parametru)
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth(0.5f)) {
            Text(txtBack)
        }
    }
}

@Composable
fun QuizMenuButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(60.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text, fontSize = 18.sp)
    }
}