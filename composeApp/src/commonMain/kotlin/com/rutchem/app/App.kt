package com.rutchem.app

import androidx.compose.foundation.layout.* // Import niezbędny dla fillMaxSize, height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.rutchem.db.DriverFactory

enum class Screen {
    WELCOME, MENU, PERIODIC_TABLE, QUIZ_MENU, QUIZ,
    TEST_MENU, TEST, TEST_RESULT, HISTORY, USER_SELECTION
}

@Composable
@Preview
fun App(driverFactory: DriverFactory) {
    val repository = remember { GameRepository(driverFactory) }
    var currentScreen by remember { mutableStateOf(Screen.WELCOME) }

    var isDarkTheme by remember { mutableStateOf(false) }
    var currentLanguage by remember { mutableStateOf(AppLanguage.PL) }

    // Zmienne stanu gry
    var selectedQuizCategory by remember { mutableStateOf(QuizCategory.ALL) }
    var testQuestionCount by remember { mutableStateOf(10) }
    var lastTestScore by remember { mutableStateOf(0) }

    MaterialTheme(colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()) {
        when (currentScreen) {
            Screen.WELCOME -> {
                WelcomeScreen(
                    repository = repository,
                    onStartClick = { currentScreen = Screen.MENU },
                    onHistoryClick = { currentScreen = Screen.HISTORY },
                    isDarkTheme = isDarkTheme,
                    onThemeChange = { isDarkTheme = it },
                    currentLanguage = currentLanguage,
                    onLanguageChange = { currentLanguage = it }
                )
            }

            Screen.MENU -> {
                MenuScreen(
                    language = currentLanguage,
                    onPeriodicTableClick = { currentScreen = Screen.PERIODIC_TABLE },
                    onQuizClick = { currentScreen = Screen.QUIZ_MENU },
                    onTestClick = { currentScreen = Screen.TEST_MENU },
                    onHistoryClick = { currentScreen = Screen.HISTORY },
                    onBack = { currentScreen = Screen.WELCOME }
                )
            }


            Screen.PERIODIC_TABLE -> PeriodicTableScreen(currentLanguage) { currentScreen = Screen.MENU }


            Screen.QUIZ_MENU -> {
                QuizMenuScreen(
                    language = currentLanguage,
                    onCategorySelected = { cat ->
                        selectedQuizCategory = cat
                        currentScreen = Screen.QUIZ
                    },
                    onBack = { currentScreen = Screen.MENU }
                )
            }

            Screen.QUIZ -> QuizScreen(selectedQuizCategory, currentLanguage) { currentScreen = Screen.QUIZ_MENU }

            Screen.TEST_MENU -> {
                TestSettingsScreen(
                    language = currentLanguage,
                    onStartTest = { cat, count ->
                        selectedQuizCategory = cat
                        testQuestionCount = count
                        currentScreen = Screen.TEST
                    },
                    //onBack = { currentScreen = Screen.MENU }
                )
            }

            Screen.TEST -> {
                TestScreen(
                    category = selectedQuizCategory,
                    questionCount = testQuestionCount,
                    language = currentLanguage,
                    onTestFinished = { score, total ->
                        lastTestScore = score
                        repository.addScore(score, total)
                        currentScreen = Screen.TEST_RESULT
                    },
                    onBack = { currentScreen = Screen.MENU }
                )
            }

            Screen.TEST_RESULT -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val endTxt = if (currentLanguage == AppLanguage.PL) "Koniec Testu!" else "Test Finished!"
                    val scoreTxt = if (currentLanguage == AppLanguage.PL) "Wynik:" else "Score:"

                    Text(endTxt, style = MaterialTheme.typography.headlineLarge)
                    Text("$scoreTxt $lastTestScore / $testQuestionCount")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { currentScreen = Screen.MENU }) {
                        Text("OK")
                    }
                }
            }

            Screen.HISTORY -> HistoryScreen(repository) { currentScreen = Screen.MENU }
            Screen.USER_SELECTION -> UserSelectionScreen(repository, { currentScreen = Screen.MENU }, { currentScreen = Screen.WELCOME })
        }
    }
}