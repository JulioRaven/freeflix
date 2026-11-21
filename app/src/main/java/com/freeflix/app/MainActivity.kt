package com.freeflix.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF090B12)
private val Panel = Color(0xFF171B27)
private val Accent = Color(0xFF8B5CF6)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { FreeFlixTheme { FreeFlixHome() } }
    }
}

@Composable
private fun FreeFlixTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Accent,
            background = Ink,
            surface = Panel,
            onBackground = Color.White,
            onSurface = Color.White
        ),
        content = content
    )
}

private data class Shelf(val title: String, val items: List<String>)

@Composable
private fun FreeFlixHome() {
    var query by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf("Accueil") }
    val shelves = listOf(
        Shelf("Reprendre la lecture", listOf("À configurer", "Bibliothèque locale")),
        Shelf("Films", listOf("Explorer le catalogue", "Vos fichiers vidéo")),
        Shelf("Séries & anime", listOf("Saisons et épisodes", "Sources autorisées"))
    )
    Box(Modifier.fillMaxSize().background(Ink)) {
        Column(Modifier.fillMaxSize()) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("FREE", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Black)
                Text("FLIX", color = Accent, fontSize = 22.sp, fontWeight = FontWeight.Black)
                Spacer(Modifier.weight(1f))
                listOf("Accueil", "Films", "Séries", "Ma liste").forEach { tab ->
                    Text(
                        tab,
                        color = if (selected == tab) Color.White else Color.LightGray,
                        modifier = Modifier.padding(horizontal = 8.dp).clickable { selected = tab },
                        fontSize = 13.sp
                    )
                }
                IconButton(onClick = { selected = "Recherche" }) {
                    Icon(Icons.Default.Search, contentDescription = "Rechercher", tint = Color.White)
                }
            }

            if (selected == "Recherche") {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp),
                    label = { Text("Rechercher un film, une série ou un anime") },
                    singleLine = true
                )
                Text(
                    "Le catalogue sera connecté dans la prochaine étape.",
                    color = Color.LightGray,
                    modifier = Modifier.padding(22.dp)
                )
            } else {
                Column(Modifier.fillMaxWidth().padding(horizontal = 22.dp)) {
                    Box(
                        Modifier.fillMaxWidth().height(250.dp)
                            .background(
                                Brush.linearGradient(listOf(Color(0xFF34205E), Color(0xFF111827), Ink)),
                                RoundedCornerShape(22.dp)
                            ).padding(24.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Column {
                            Text("VOTRE CINÉMA, PARTOUT", color = Color(0xFFC4B5FD), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            Text("Tout commence ici.", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                            Text("Films • Séries • Anime", color = Color.LightGray)
                            Spacer(Modifier.height(14.dp))
                            Button(onClick = { selected = "Recherche" }) { Text("Explorer") }
                        }
                    }
                    Spacer(Modifier.height(26.dp))
                    shelves.forEach { shelf ->
                        Text(shelf.title, color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(shelf.items) { item ->
                                Box(
                                    Modifier.width(150.dp).height(104.dp)
                                        .background(Panel, RoundedCornerShape(14.dp))
                                        .clickable { selected = "Recherche" }
                                        .padding(14.dp),
                                    contentAlignment = Alignment.BottomStart
                                ) {
                                    Text(item, color = Color.White, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                        Spacer(Modifier.height(22.dp))
                    }
                }
            }
        }
    }
}
