package com.example.therickandmorty.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import com.example.therickandmorty.data.local.extensions.toData
import com.example.therickandmorty.data.remote.dtos.CharacterDto
import com.example.therickandmorty.presentation.components.appHeader
import com.example.therickandmorty.presentation.viewmodel.FavoritesViewModel
import com.example.therickandmorty.presentation.viewmodel.actions.FavoritesAction
import org.koin.androidx.compose.koinViewModel

data class CharacterDetailsScreen(
    val character: CharacterDto,
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val favoritesViewModel: FavoritesViewModel = koinViewModel()

        val state by favoritesViewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(character.id) {
            favoritesViewModel.onAction(FavoritesAction.IsFavorite(characterId = character.id))
        }

        Scaffold(
            topBar = {
                appHeader(
                    title = "Character Details",
                    showBack = true,
                    onBackClick = { navigator.pop() },
                    actions = {
                        IconButton(
                            onClick = {
                                favoritesViewModel.onAction(
                                    FavoritesAction.ToggleFavorite(
                                        character = character.toData(),
                                    ),
                                )
                            },
                        ) {
                            Icon(
                                imageVector =
                                    if (state.isFavorite) Icons.Default.Check else Icons.Default.Add,
                                contentDescription = if (state.isFavorite) "Favorito" else "Não favorito",
                                tint = Color.Black,
                            )
                        }
                    },
                )
            },
        ) { innerPadding ->
            characterDetailsContent(
                modifier =
                    Modifier
                        .padding(top = innerPadding.calculateTopPadding()),
                character = character,
            )
        }
    }
}

@Composable
fun characterDetailsContent(
    modifier: Modifier = Modifier,
    character: CharacterDto,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        AsyncImage(
            model = character.image,
            contentDescription = character.name,
            modifier =
                Modifier
                    .size(280.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(8.dp),
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = character.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
        )

        Spacer(modifier = Modifier.height(16.dp))

        detailsCard(character = character)

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun detailsCard(character: CharacterDto) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AssistChip(
                    onClick = {},
                    colors =
                        AssistChipDefaults.assistChipColors(
                            containerColor =
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
                    AssistChip(
                        onClick = {},
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
}

@Preview(showBackground = true)
@Composable
fun characterDetailsContentPreview() {
    MaterialTheme {
        val character =
            CharacterDto(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                gender = "Male",
                type = "Scientist",
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            )

        characterDetailsContent(
            character = character,
        )
    }
}
