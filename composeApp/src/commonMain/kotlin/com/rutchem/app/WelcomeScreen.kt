package com.rutchem.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rutchem.db.UserEntity

@Composable
fun WelcomeScreen(
    repository: GameRepository,
    onStartClick: () -> Unit,
    onHistoryClick: () -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    currentLanguage: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit
) {
    // Stan widoczności okna ustawień (zarządzany wewnątrz ekranu)
    var showSettingsDialog by remember { mutableStateOf(false) }

    // Pobieramy imię aktywnego gracza do wyświetlenia na ekranie głównym
    // Używamy key(showSettingsDialog), żeby odświeżyć po zamknięciu ustawień
    var activeUserName by remember(showSettingsDialog) {
        mutableStateOf(repository.getActiveUser()?.name ?: "Wybierz gracza")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- 1. PRZYCISK USTAWIEŃ (Prawy Górny Róg) ---
        IconButton(
            onClick = { showSettingsDialog = true },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Ustawienia",
                modifier = Modifier.size(32.dp)
            )
        }

        // --- 2. GŁÓWNA ZAWARTOŚĆ (Środek) ---
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "RutChem",
                style = MaterialTheme.typography.displayMedium
            )

            // Wyświetlenie aktualnego gracza
            Text(
                text = if (currentLanguage == AppLanguage.PL) "Gracz: $activeUserName" else "Player: $activeUserName",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Przycisk START
            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(56.dp),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Text(
                    text = if (currentLanguage == AppLanguage.PL) "START" else "START",
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // --- 3. OKNO DIALOGOWE USTAWIEŃ ---
        if (showSettingsDialog) {
            SettingsDialog(
                onDismiss = { showSettingsDialog = false },
                repository = repository,
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                currentLanguage = currentLanguage,
                onLanguageChange = onLanguageChange
            )
        }
    }
}

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    repository: GameRepository,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    currentLanguage: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit
) {
    // Lokalne stany dla okna dialogowego
    var users by remember { mutableStateOf(repository.getUsers()) }
    var activeUserId by remember { mutableStateOf(repository.getActiveUser()?.id) }
    var newUserText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (currentLanguage == AppLanguage.PL) "Ustawienia" else "Settings") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // A. JĘZYK
                Text(
                    text = if (currentLanguage == AppLanguage.PL) "Język / Language" else "Language",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = currentLanguage == AppLanguage.PL,
                        onClick = { onLanguageChange(AppLanguage.PL) },
                        label = { Text("Polski") },
                        leadingIcon = if (currentLanguage == AppLanguage.PL) {
                            { Icon(Icons.Default.Check, null) }
                        } else null
                    )
                    FilterChip(
                        selected = currentLanguage == AppLanguage.EN,
                        onClick = { onLanguageChange(AppLanguage.EN) },
                        label = { Text("English") },
                        leadingIcon = if (currentLanguage == AppLanguage.EN) {
                            { Icon(Icons.Default.Check, null) }
                        } else null
                    )
                }

                HorizontalDivider()

                // B. MOTYW
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (currentLanguage == AppLanguage.PL) "Ciemny motyw" else "Dark Mode",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = onThemeChange
                    )
                }

                HorizontalDivider()

                // C. UŻYTKOWNICY
                Text(
                    text = if (currentLanguage == AppLanguage.PL) "Wybierz gracza" else "Select Player",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                // Dodawanie
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = newUserText,
                        onValueChange = { newUserText = it },
                        placeholder = { Text(if (currentLanguage == AppLanguage.PL) "Nowy gracz..." else "New player...") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    IconButton(onClick = {
                        if (newUserText.isNotBlank()) {
                            repository.addUser(newUserText)
                            users = repository.getUsers() // Odśwież listę
                            // Auto-select nowego
                            val added = users.lastOrNull()
                            if (added != null) {
                                repository.setActiveUser(added.id)
                                activeUserId = added.id
                            }
                            newUserText = ""
                        }
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Dodaj")
                    }
                }

                // Lista
                LazyColumn(
                    modifier = Modifier
                        .heightIn(max = 150.dp) // Ograniczenie wysokości listy
                        .fillMaxWidth()
                ) {
                    items(users) { user ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    repository.setActiveUser(user.id)
                                    activeUserId = user.id
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (user.id == activeUserId) Icons.Default.Check else Icons.Default.Person,
                                contentDescription = null,
                                tint = if (user.id == activeUserId) MaterialTheme.colorScheme.primary else Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = user.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (user.id == activeUserId) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}