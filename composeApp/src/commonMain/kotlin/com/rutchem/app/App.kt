package com.rutchem.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class Screen {
    WELCOME, MODES, PERIODIC_TABLE, QUIZ_MENU, QUIZ,
    TEST_SETTINGS, TEST_RUN, TEST_RESULT // <-- Nowe ekrany
}

enum class AppLanguage { PL, EN }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    var currentScreen by remember { mutableStateOf(Screen.WELCOME) }

    // Zmienne dla Quizu (trening)
    var selectedQuizCategory by remember { mutableStateOf(QuizCategory.ALL) }

    // Zmienne dla Testu
    var testCategory by remember { mutableStateOf(QuizCategory.ALL) }
    var testQuestionCount by remember { mutableStateOf(10) }
    var testScore by remember { mutableStateOf(0) } // Wynik ostatniego testu

    var isDarkTheme by remember { mutableStateOf(false) }
    var currentLanguage by remember { mutableStateOf(AppLanguage.PL) }
    val colors = if (isDarkTheme) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = colors) {
        Scaffold(
            topBar = {
                if (currentScreen != Screen.WELCOME && currentScreen != Screen.TEST_RESULT) {
                    // Ukrywamy pasek na ekranie wyniku, żeby user musiał kliknąć "Wróć"
                    val title = when(currentScreen) {
                        Screen.PERIODIC_TABLE -> if(currentLanguage == AppLanguage.PL) "Tablica Mendelejewa" else "Periodic Table"
                        Screen.QUIZ_MENU -> if(currentLanguage == AppLanguage.PL) "Trening" else "Practice"
                        Screen.TEST_SETTINGS -> if(currentLanguage == AppLanguage.PL) "Ustawienia Testu" else "Test Settings"
                        Screen.TEST_RUN -> "Test"
                        else -> "Menu"
                    }

                    TopAppBar(
                        title = { Text(title) },
                        navigationIcon = {
                            // Blokujemy cofanie w trakcie trwania testu (opcjonalnie)
                            if (currentScreen != Screen.TEST_RUN) {
                                IconButton(onClick = {
                                    currentScreen = when (currentScreen) {
                                        Screen.PERIODIC_TABLE, Screen.QUIZ_MENU, Screen.TEST_SETTINGS -> Screen.MODES
                                        Screen.QUIZ -> Screen.QUIZ_MENU
                                        else -> Screen.WELCOME
                                    }
                                }) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    )
                }
            }
        ) { paddingValues ->
            Surface(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
                when (currentScreen) {
                    Screen.WELCOME -> {
                        WelcomeScreen(
                            onStartClick = { currentScreen = Screen.MODES },
                            isDarkTheme = isDarkTheme, onThemeChange = { isDarkTheme = !isDarkTheme },
                            currentLanguage = currentLanguage, onLanguageChange = { currentLanguage = it }
                        )
                    }
                    Screen.MODES -> {
                        MenuScreen(
                            language = currentLanguage,
                            onPeriodicTableClick = { currentScreen = Screen.PERIODIC_TABLE },
                            onQuizClick = { currentScreen = Screen.QUIZ_MENU },
                            onTestClick = { currentScreen = Screen.TEST_SETTINGS } // <-- Idziemy do testu
                        )
                    }
                    Screen.PERIODIC_TABLE -> PeriodicTableScreen(language = currentLanguage)

                    // --- TRYB TRENINGOWY ---
                    Screen.QUIZ_MENU -> {
                        QuizMenuScreen(language = currentLanguage) { cat ->
                            selectedQuizCategory = cat
                            currentScreen = Screen.QUIZ
                        }
                    }
                    Screen.QUIZ -> QuizScreen(category = selectedQuizCategory, language = currentLanguage)

                    // --- TRYB TESTU ---
                    Screen.TEST_SETTINGS -> {
                        TestSettingsScreen(language = currentLanguage) { cat, count ->
                            testCategory = cat
                            testQuestionCount = count
                            currentScreen = Screen.TEST_RUN
                        }
                    }
                    Screen.TEST_RUN -> {
                        TestScreen(
                            category = testCategory,
                            questionCount = testQuestionCount,
                            language = currentLanguage,
                            onTestFinished = { score, _ ->
                                testScore = score
                                currentScreen = Screen.TEST_RESULT
                            }
                        )
                    }
                    Screen.TEST_RESULT -> {
                        TestResultScreen(
                            score = testScore,
                            total = testQuestionCount,
                            language = currentLanguage,
                            onBackToMenu = { currentScreen = Screen.MODES }
                        )
                    }
                }
            }
        }
    }
}