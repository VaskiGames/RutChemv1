package com.rutchem.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuizMenuScreen(
    language: AppLanguage, // <--- Odbieramy język
    onCategorySelected: (QuizCategory) -> Unit
) {
    val title = if (language == AppLanguage.PL) "Wybierz kategorię:" else "Select category:"
    val tSymbol = if (language == AppLanguage.PL) "Symbol Chemiczny" else "Chemical Symbol"
    val tMass = if (language == AppLanguage.PL) "Masa Atomowa" else "Atomic Mass"
    val tNumber = if (language == AppLanguage.PL) "Liczba Atomowa" else "Atomic Number"
    val tAll = if (language == AppLanguage.PL) "Wszystko" else "All"

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(title, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 32.dp))

        QuizMenuButton(tSymbol) { onCategorySelected(QuizCategory.SYMBOL) }
        Spacer(modifier = Modifier.height(12.dp))
        QuizMenuButton(tMass) { onCategorySelected(QuizCategory.MASS) }
        Spacer(modifier = Modifier.height(12.dp))
        QuizMenuButton(tNumber) { onCategorySelected(QuizCategory.NUMBER) }
        Spacer(modifier = Modifier.height(12.dp))
        QuizMenuButton(tAll) { onCategorySelected(QuizCategory.ALL) }
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