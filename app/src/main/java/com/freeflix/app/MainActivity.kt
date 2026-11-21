package com.freeflix.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.freeflix.app.catalog.CatalogItem
import com.freeflix.app.catalog.CatalogViewModel
import com.freeflix.app.catalog.MediaKind

private val Ink = Color(0xFF090B12)
private val Panel = Color(0xFF171B27)
private val Accent = Color(0xFF8B5CF6)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { FreeFlixTheme { FreeFlixApp() } }
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

@Composable
private fun FreeFlixApp(vm: CatalogViewModel = viewModel()) {
    val state by vm.state.collectAsStateWithLifecycle()
    var query by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("Pour vous") }
    var selectedItem by remember { mutableStateOf<CatalogItem?>(null) }

    Surface(Modifier.fillMaxSize(), color = Ink) {
        Column(Modifier.fillMaxSize()) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("FREE", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                Text("FLIX", color = Accent, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { selectedItem = null; selectedTab = "Recherche" }) {
                    Icon(Icons.Default.Search, contentDescription = "Recherche", tint = Color.White)
                }
            }

            if (selectedItem != null) {
                DetailScreen(
                    item = selectedItem!!,
                    overview = state.detailOverview,
                    loading = state.loadingDetail,
                    onBack = { selectedItem = null },
                    onFindSources = { vm.loadSourcesFor(selectedItem!!) }
                )
            } else {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf("Pour vous", "Films", "Séries", "Anime", "Recherche").forEach { tab ->
                        FilterChip(
                            selected = selectedTab == tab,
                            onClick = {
                                selectedTab = tab
                                if (tab == "Pour vous" || tab == "Films") vm.loadMovies()
                                if (tab == "Séries" || tab == "Anime") vm.loadTv()
                            },
                            label = { Text(tab) }
                        )
                    }
                }

                if (selectedTab == "Recherche") {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        singleLine = true,
                        label = { Text("Film, série, anime…") },
                        trailingIcon = {
                            IconButton(onClick = { vm.search(query) }) {
                                Icon(Icons.Default.Search, contentDescription = "Lancer la recherche")
                            }
                        }
                    )
                }

                if (state.error != null) {
                    Text(state.error!!, color = Color(0xFFFF9B9B), modifier = Modifier.padding(20.dp))
                }
                if (state.loading) {
                    LinearProgressIndicator(Modifier.fillMaxWidth().padding(horizontal = 20.dp))
                }
                if (state.items.isEmpty() && !state.loading) {
                    EmptyCatalog(onRetry = { vm.loadMovies() })
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 32.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Box(
                                Modifier.fillMaxWidth().height(172.dp)
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(Brush.linearGradient(listOf(Color(0xFF382166), Color(0xFF171B27), Ink)))
                                    .padding(22.dp),
                                contentAlignment = Alignment.BottomStart
                            ) {
                                Column {
                                    Text("FILMS • SÉRIES • ANIME", color = Color(0xFFD6C7FF), style = MaterialTheme.typography.labelMedium)
                                    Spacer(Modifier.height(8.dp))
                                    Text("Votre prochain coup de cœur.", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                                    Text("Explorez le catalogue.", color = Color.LightGray)
                                }
                            }
                        }
                        item {
                            Text(
                                when (selectedTab) {
                                    "Recherche" -> "Résultats"
                                    "Films" -> "Films populaires"
                                    "Séries", "Anime" -> "Séries populaires"
                                    else -> "À découvrir"
                                },
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        items(state.items, key = { "${it.kind}-${it.id}" }) { item ->
                            CatalogRow(item = item, onClick = { selectedItem = item; vm.loadDetails(item) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CatalogRow(item: CatalogItem, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Panel)
            .clickable(onClick = onClick).padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        AsyncImage(
            model = item.posterUrl,
            contentDescription = item.title,
            modifier = Modifier.width(86.dp).height(126.dp).clip(RoundedCornerShape(10.dp))
        )
        Column(Modifier.weight(1f).align(Alignment.CenterVertically)) {
            Text(item.title, color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(item.year ?: item.kind.label, color = Color.LightGray, style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(8.dp))
            Text(item.overview.ifBlank { "Aucun résumé disponible." }, color = Color(0xFFB8BBC8), maxLines = 3, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun DetailScreen(
    item: CatalogItem,
    overview: String?,
    loading: Boolean,
    onBack: () -> Unit,
    onFindSources: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        TextButton(onClick = onBack, modifier = Modifier.padding(horizontal = 12.dp)) {
            Icon(Icons.Default.ArrowBack, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text("Retour")
        }
        AsyncImage(
            model = item.backdropUrl ?: item.posterUrl,
            contentDescription = item.title,
            modifier = Modifier.fillMaxWidth().height(220.dp)
        )
        Column(Modifier.padding(20.dp)) {
            Text(item.title, color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(listOfNotNull(item.year, item.kind.label).joinToString(" • "), color = Color.LightGray)
            Spacer(Modifier.height(14.dp))
            Text(overview ?: item.overview.ifBlank { "Résumé indisponible." }, color = Color(0xFFD5D6DE))
            Spacer(Modifier.height(20.dp))
            Button(onClick = onFindSources, modifier = Modifier.fillMaxWidth()) {
                Text("Rechercher des sources")
            }
            Text(
                "La lecture sera proposée lorsqu'une source compatible et autorisée sera disponible.",
                color = Color.LightGray,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 10.dp)
            )
            if (loading) LinearProgressIndicator(Modifier.fillMaxWidth().padding(top = 12.dp))
        }
    }
}

@Composable
private fun EmptyCatalog(onRetry: () -> Unit) {
    Column(Modifier.fillMaxWidth().padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Le catalogue attend sa configuration.", color = Color.White)
        Text("Ajoutez votre clé API TMDB pour charger les films et séries.", color = Color.LightGray)
        Spacer(Modifier.height(12.dp))
        OutlinedButton(onClick = onRetry) { Text("Réessayer") }
    }
}
