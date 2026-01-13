package com.rutchem.app

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeScreen(
    onStartClick: () -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit,
    currentLanguage: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit
) {
    // Stan: czy menu opcji jest rozwinięte?
    var showMenu by remember { mutableStateOf(false) }

    // Teksty zależne od języka
    val titleText = if (currentLanguage == AppLanguage.PL) "Witaj w RutChem!" else "Welcome to RutChem!"
    val buttonText = if (currentLanguage == AppLanguage.PL) "Rozpocznij" else "Start"
    val themeText = if (currentLanguage == AppLanguage.PL) "Tryb Ciemny" else "Dark Mode"
    val langText = if (currentLanguage == AppLanguage.PL) "Język: PL" else "Language: EN"

    // Główny kontener Box pozwala nakładać elementy
    Box(modifier = Modifier.fillMaxSize()) {

        // 1. PRZYCISK USTAWIEŃ (Prawy górny róg)
        Box(modifier = Modifier.align(Alignment.TopEnd)) {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Opcje",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            // Rozwijane menu
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                // Opcja 1: Zmiana języka
                DropdownMenuItem(
                    text = { Text(langText) },
                    onClick = {
                        // Przełączamy język na przeciwny
                        val newLang = if (currentLanguage == AppLanguage.PL) AppLanguage.EN else AppLanguage.PL
                        onLanguageChange(newLang)
                        showMenu = false
                    }
                )

                // Opcja 2: Zmiana motywu (Switch)
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(themeText)
                            Spacer(modifier = Modifier.width(8.dp))
                            Switch(
                                checked = isDarkTheme,
                                onCheckedChange = { onThemeChange() } // Wywołanie zmiany motywu
                            )
                        }
                    },
                    onClick = {
                        // Puste, bo kliknięcie obsługuje Switch, ale można też tu:
                        onThemeChange()
                    }
                )
            }
        }

        // 2. GŁÓWNA ZAWARTOŚĆ (Środek ekranu)
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titleText,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onStartClick,
                modifier = Modifier.fillMaxWidth(0.6f).height(50.dp)
            ) {
                Text(buttonText)
            }
        }
    }
}