package com.rutchem.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

// Stałe rozmiary
val CELL_SIZE = 65.dp
val CELL_PADDING = 2.dp
// Całkowita szerokość i wysokość tabeli (18 kolumn, 10 rzędów)
val TABLE_WIDTH = 18 * 65f
val TABLE_HEIGHT = 10 * 65f

@Composable
fun PeriodicTableScreen(language: AppLanguage) {
    var selectedElement by remember { mutableStateOf<Element?>(null) }

    // --- STAN ZOOMU I PRZESUNIĘCIA ---
    var scale by remember { mutableStateOf(0.5f) } // Startujemy trochę oddaleni
    var offset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds() // Ważne: nie rysuj poza ekranem
            .pointerInput(Unit) {
                // Detektor gestów (przesuwanie dwoma palcami, przybliżanie)
                detectTransformGestures { _, pan, zoom, _ ->
                    scale *= zoom
                    // Ograniczniki zoomu (nie za blisko, nie za daleko)
                    scale = scale.coerceIn(0.3f, 3f)

                    // Przesuwanie (skalujemy przesunięcie, żeby działało naturalnie)
                    offset += pan
                }
            }
    ) {
        // --- WARSTWA TABELI ---
        Box(
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
        ) {
            // Rysujemy elementy
            periodicTableData.forEach { element ->
                val pos = getElementPosition(element)
                val offsetX = (pos.col - 1) * CELL_SIZE.value
                val offsetY = (pos.row - 1) * CELL_SIZE.value

                ElementCell(
                    element = element,
                    modifier = Modifier
                        .offset(x = offsetX.dp, y = offsetY.dp)
                        .size(CELL_SIZE)
                        .padding(CELL_PADDING),
                    onClick = { selectedElement = element }
                )
            }
        }

        // --- PRZYCISK RESETOWANIA WIDOKU (opcjonalny) ---
        // Pływający przycisk, żeby wrócić do centrum, jak się zgubimy
        FloatingActionButton(
            onClick = {
                scale = 0.5f
                offset = Offset.Zero
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .zIndex(10f), // Zawsze na wierzchu
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Text("Reset", color = MaterialTheme.colorScheme.onPrimary)
        }

        // --- DYMEK ZE SZCZEGÓŁAMI ---
        if (selectedElement != null) {
            ElementDetailDialog(
                element = selectedElement!!,
                language = language,
                onDismiss = { selectedElement = null }
            )
        }
    }
}

// --- RESZTA FUNKCJI BEZ ZMIAN (ElementCell, ElementDetailDialog, getElementPosition, getElementColor) ---
// (Wklej tutaj te same funkcje pomocnicze co w poprzednim kroku, bo są idealne)

@Composable
fun ElementCell(element: Element, modifier: Modifier, onClick: () -> Unit) {
    val backgroundColor = getElementColor(element)

    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(2.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(2.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${element.number}",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                lineHeight = 10.sp
            )
            Text(
                text = element.symbol,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(
                text = "%.2f".format(element.mass),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 8.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
fun ElementDetailDialog(element: Element, language: AppLanguage, onDismiss: () -> Unit) {
    val nameLabel = if(language == AppLanguage.PL) "Nazwa" else "Name"
    val massLabel = if(language == AppLanguage.PL) "Masa" else "Mass"
    val groupLabel = if(language == AppLanguage.PL) "Grupa" else "Group"
    val numberLabel = if(language == AppLanguage.PL) "Liczba atomowa" else "Atomic Number"

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("OK") }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(getElementColor(element), shape = RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(element.symbol, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(element.getName(language), style = MaterialTheme.typography.headlineSmall)
                    Text("$numberLabel: ${element.number}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                }
            }
        },
        text = {
            Column {
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                DetailRow(nameLabel, element.getName(language))
                DetailRow("Symbol", element.symbol)
                DetailRow(numberLabel, "${element.number}")
                DetailRow(massLabel, "${element.mass} u")
                DetailRow(groupLabel, "${element.group}")
            }
        }
    )
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = FontWeight.Bold)
        Text(value)
    }
}

data class GridPos(val row: Int, val col: Int)

fun getElementPosition(element: Element): GridPos {
    val n = element.number
    if (n in 57..71) return GridPos(row = 9, col = n - 57 + 3)
    if (n in 89..103) return GridPos(row = 10, col = n - 89 + 3)

    return when {
        n == 1 -> GridPos(1, 1)
        n == 2 -> GridPos(1, 18)
        n in 3..4 -> GridPos(2, n - 2)
        n in 5..10 -> GridPos(2, n + 8)
        n in 11..12 -> GridPos(3, n - 10)
        n in 13..18 -> GridPos(3, n)
        n in 19..36 -> GridPos(4, element.group)
        n in 37..54 -> GridPos(5, element.group)
        n in 55..56 -> GridPos(6, element.group)
        n in 72..86 -> GridPos(6, element.group)
        n in 87..88 -> GridPos(7, element.group)
        n in 104..118 -> GridPos(7, element.group)
        else -> GridPos(0, 0)
    }
}

fun getElementColor(element: Element): Color {
    val n = element.number
    return when {
        element.group == 1 && n != 1 -> Color(0xFFFFD700)
        element.group == 2 -> Color(0xFFEEE8AA)
        n in 57..71 -> Color(0xFFDEB887)
        n in 89..103 -> Color(0xFFB0C4DE)
        element.group in 3..12 -> Color(0xFFFFE4C4)
        element.group == 13 -> Color(0xFF98FB98)
        element.group == 14 -> Color(0xFF90EE90)
        element.group == 15 -> Color(0xFFADFF2F)
        element.group == 16 -> Color(0xFF7FFF00)
        element.group == 17 -> Color(0xFF7CFC00)
        element.group == 18 -> Color(0xFF87CEFA)
        n == 1 -> Color(0xFFADFF2F)
        else -> Color.LightGray
    }
}