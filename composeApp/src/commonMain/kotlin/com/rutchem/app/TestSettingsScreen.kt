package com.rutchem.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TestSettingsScreen(
    language: AppLanguage,
    onStartTest: (QuizCategory, Int) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(QuizCategory.ALL) }
    var selectedQuestionCount by remember { mutableStateOf(10) }

    // Tłumaczenia
    val title = if (language == AppLanguage.PL) "Konfiguracja Testu" else "Test Setup"
    val catLabel = if (language == AppLanguage.PL) "Kategoria:" else "Category:"
    val countLabel = if (language == AppLanguage.PL) "Liczba pytań:" else "Number of questions:"
    val startBtn = if (language == AppLanguage.PL) "Rozpocznij Test" else "Start Test"

    val tSymbol = if (language == AppLanguage.PL) "Symbole" else "Symbols"
    val tMass = if (language == AppLanguage.PL) "Masa" else "Mass"
    val tNumber = if (language == AppLanguage.PL) "Liczba at." else "At. Number"
    val tAll = if (language == AppLanguage.PL) "Wszystko" else "All"

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(32.dp))

        // --- WYBÓR KATEGORII ---
        Text(catLabel, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            FilterChip(selected = selectedCategory == QuizCategory.SYMBOL, onClick = { selectedCategory = QuizCategory.SYMBOL }, label = { Text(tSymbol) })
            FilterChip(selected = selectedCategory == QuizCategory.MASS, onClick = { selectedCategory = QuizCategory.MASS }, label = { Text(tMass) })
        }
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            FilterChip(selected = selectedCategory == QuizCategory.NUMBER, onClick = { selectedCategory = QuizCategory.NUMBER }, label = { Text(tNumber) })
            FilterChip(selected = selectedCategory == QuizCategory.ALL, onClick = { selectedCategory = QuizCategory.ALL }, label = { Text(tAll) })
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- WYBÓR LICZBY PYTAŃ ---
        Text(countLabel, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            listOf(5, 10, 15, 20).forEach { count ->
                FilterChip(
                    selected = selectedQuestionCount == count,
                    onClick = { selectedQuestionCount = count },
                    label = { Text("$count") }
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { onStartTest(selectedCategory, selectedQuestionCount) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(startBtn, fontSize = 18.sp)
        }
    }
}