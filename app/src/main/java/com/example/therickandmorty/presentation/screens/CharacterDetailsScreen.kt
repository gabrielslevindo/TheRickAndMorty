@file:Suppress("FunctionNaming", "NoWildcardImports")

package com.example.therickandmorty.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.therickandmorty.data.local.extensions.toData
import com.example.therickandmorty.data.remote.dtos.CharacterDto
import com.example.therickandmorty.presentation.viewmodel.FavoritesViewModel
import org.koin.androidx.compose.koinViewModel

data class CharacterDetailsScreen(
    val character: CharacterDto,
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val favoritesViewModel: FavoritesViewModel = koinViewModel()

        var isFavorite by remember { mutableStateOf(false) }

        LaunchedEffect(character.id) {
            isFavorite = favoritesViewModel.isFavorite(character.id)
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Character Details") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                favoritesViewModel.toggleFavorite(character.toData())
                                isFavorite = !isFavorite
                            },
                        ) {
                            Icon(
                                imageVector =
                                    if (isFavorite) Icons.Default.Check else Icons.Default.Add,
                                contentDescription = if (isFavorite) "Favorito" else "Não favorito",
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    },
                )
            },
        ) { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                val painter =
                    rememberAsyncImagePainter(
                        ImageRequest
                            .Builder(LocalContext.current)
                            .data(character.image)
                            .crossfade(true)
                            .build(),
                    )
                Image(
                    painter = painter,
                    contentDescription = character.name,
                    modifier =
                        Modifier
                            .size(300.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .shadow(8.dp, RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                    contentScale = ContentScale.Crop,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = character.name,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .shadow(4.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        ),
                ) {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Chip(
                                onClick = {},
                                colors =
                                    ChipDefaults.chipColors(
                                        backgroundColor =
                                            when (character.status.lowercase()) {
                                                "alive" -> Color.Green
                                                "dead" -> Color.Red
                                                else -> MaterialTheme.colorScheme.tertiary
                                            },
                                    ),
                                label = {
                                    Text(
                                        text = character.status,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                    )
                                },
                            )

                            if (character.type.isNotEmpty()) {
                                Chip(
                                    onClick = {},
                                    colors =
                                        ChipDefaults.chipColors(
                                            backgroundColor = MaterialTheme.colorScheme.secondary,
                                        ),
                                    label = { Text(character.type) },
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            "Species: ${character.species}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Gender: ${character.gender}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
