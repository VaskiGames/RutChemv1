package com.rutchem.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rutchem.db.ScoreEntity

@Composable
fun HistoryScreen(
    repository: GameRepository,
    onBack: () -> Unit
) {
    // Pobieramy historię dla aktualnego użytkownika
    // Używamy remember, żeby nie pobierać danych przy każdym odświeżeniu klatki

    var scores by remember { mutableStateOf(repository.getHistoryForActiveUser()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = onBack) {
            Text("Wróć")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Historia Gier",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (scores.isEmpty()) {
            Text("Brak zapisanych wyników dla tego gracza.")
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(scores) { score ->
                    ScoreItem(score)
                }
            }
        }
    }
}

@Composable
fun ScoreItem(score: ScoreEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Wynik: ${score.score} / ${score.totalQuestions}",
                style = MaterialTheme.typography.titleMedium
            )


        }
    }
}