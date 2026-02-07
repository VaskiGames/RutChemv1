package com.rutchem.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import com.rutchem.db.UserEntity

@Composable
fun UserSelectionScreen(
    repository: GameRepository,
    onUserSelected: () -> Unit,
    onBack: () -> Unit
) {
    // Ważne: repository.getUsers() zwraca List<UserEntity>
    var newUserName by remember { mutableStateOf("") }
    var users by remember { mutableStateOf(repository.getUsers()) }

    Column {
        Button(onClick = onBack) { Text("Wróć") }

        TextField(
            value = newUserName,
            onValueChange = { newUserName = it },
            label = { Text("Nowy gracz") }
        )

        Button(onClick = {
            if (newUserName.isNotBlank()) {
                repository.addUser(newUserName)
                users = repository.getUsers() // Odśwież listę
                newUserName = ""
            }
        }) {
            Text("Dodaj")
        }

        LazyColumn {
            items(users) { user ->
                Button(onClick = {
                    repository.setActiveUser(user.id)
                    onUserSelected()
                }) {
                    Text(user.name)
                }
            }
        }
    }
}