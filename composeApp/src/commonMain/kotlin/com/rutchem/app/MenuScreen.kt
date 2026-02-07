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
fun MenuScreen(
    language: AppLanguage,
    onPeriodicTableClick: () -> Unit,
    onQuizClick: () -> Unit,
    onTestClick: () -> Unit,
    onHistoryClick: () -> Unit, // <--- UJEDNOLICONA NAZWA
    onBack: () -> Unit
) {
    // Tłumaczenia tekstów
    val textChoose = if (language == AppLanguage.PL) "Wybierz narzędzie:" else "Choose a tool:"
    val textTable = if (language == AppLanguage.PL) "Tablica Mendelejewa" else "Periodic Table"
    val textQuiz = if (language == AppLanguage.PL) "Quiz (Trening)" else "Quiz (Practice)"
    val textTest = if (language == AppLanguage.PL) "Test Wiedzy" else "Knowledge Test"
    val textHistory = if (language == AppLanguage.PL) "Wyniki / Historia" else "Score History"
    val textLogout = if (language == AppLanguage.PL) "Wyloguj" else "Log out"

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(textChoose, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(32.dp))

        // 1. Tablica
        MenuButton(textTable, onClick = onPeriodicTableClick)
        Spacer(modifier = Modifier.height(16.dp))

        // 2. Quiz
        MenuButton(textQuiz, onClick = onQuizClick)
        Spacer(modifier = Modifier.height(16.dp))

        // 3. Test
        MenuButton(textTest, onClick = onTestClick)
        Spacer(modifier = Modifier.height(16.dp))

        // 4. Historia (Teraz nazwa parametru pasuje do nazwy zmiennej)
        MenuButton(textHistory, onClick = onHistoryClick)

        Spacer(modifier = Modifier.height(48.dp))

        // 5. Wyloguj
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(textLogout, fontSize = 16.sp)
        }
    }
}

@Composable
fun MenuButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(55.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text, fontSize = 18.sp)
    }
}