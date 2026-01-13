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
fun MenuScreen(
    language: AppLanguage,
    onPeriodicTableClick: () -> Unit,
    onQuizClick: () -> Unit,
    onTestClick: () -> Unit
) {
    // Tłumaczenia tekstów
    val textChoose = if (language == AppLanguage.PL) "Wybierz narzędzie:" else "Choose a tool:"
    val textTable = if (language == AppLanguage.PL) "Tablica Mendelejewa" else "Periodic Table"
    val textQuiz = if (language == AppLanguage.PL) "Quiz (Trening)" else "Quiz (Practice)"
    val textTest = if (language == AppLanguage.PL) "Test Wiedzy" else "Knowledge Test"
    val textCalc = if (language == AppLanguage.PL) "Wyniki" else "Score"
    val textUnit = if (language == AppLanguage.PL) "Przelicznik (Wkrótce)" else "Converter (Soon)"

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(textChoose, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))

        MenuButton(textTable, onClick = onPeriodicTableClick)
        Spacer(modifier = Modifier.height(10.dp))

        MenuButton(textQuiz, onClick = onQuizClick)
        Spacer(modifier = Modifier.height(10.dp))

        MenuButton(textTest, onClick = onTestClick)

        Spacer(modifier = Modifier.height(10.dp))
        MenuButton(textCalc, onClick = onScoreClick) // Ten przycisk nie robi nic
        Spacer(modifier = Modifier.height(10.dp))
        MenuButton(textUnit) {}
    }
}

@Composable
fun MenuButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text, fontSize = 16.sp)
    }
}