package com.equipe.pokedex.userinterface

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.equipe.pokedex.data.PokemonItem
import com.equipe.pokedex.viewmodel.DetalheUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDetalhe(
    pokemonId: Int,
    state: DetalheUiState,
    ehFavorito: Boolean,
    onCarregar: (Int) -> Unit,
    onAlternarFavorito: (PokemonItem) -> Unit,
    onVoltar: () -> Unit
) {
    LaunchedEffect(pokemonId) {
        onCarregar(pokemonId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes #${"%03d".format(pokemonId)}", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VermelhoPokedex)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is DetalheUiState.Carregando -> {
                    CircularProgressIndicator(color = VermelhoPokedex)
                }
                is DetalheUiState.Erro -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = state.mensagem, color = VermelhoPokedex)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { onCarregar(pokemonId) },
                            colors = ButtonDefaults.buttonColors(containerColor = VermelhoPokedex)
                        ) {
                            Text("Tentar novamente", color = Color.White)
                        }
                    }
                }
                is DetalheUiState.Sucesso -> {
                    val pokemon = state.pokemon
                    val corTipo = corPorTipo(pokemon.tipos.firstOrNull() ?: "")

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = pokemon.imagemUrl,
                            contentDescription = pokemon.nome,
                            modifier = Modifier.size(180.dp)
                        )
                        Text(text = pokemon.nome, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            pokemon.tipos.forEach { tipo ->
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(corPorTipo(tipo))
                                        .padding(horizontal = 14.dp, vertical = 4.dp)
                                ) {
                                    Text(text = tipo, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text("Altura: ${pokemon.altura / 10.0} m", fontSize = 16.sp)
                            Text("Peso: ${pokemon.peso / 10.0} kg", fontSize = 16.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        pokemon.stats.forEach { (statNome, valor) ->
                            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = statNome.uppercase(), fontSize = 12.sp)
                                    Text(text = valor.toString(), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                                LinearProgressIndicator(
                                    progress = { (valor / 150f).coerceIn(0f, 1f) },
                                    color = corTipo,
                                    modifier = Modifier.fillMaxWidth().height(8.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = { onAlternarFavorito(pokemon) },
                            colors = ButtonDefaults.buttonColors(containerColor = VermelhoPokedex),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = if (ehFavorito) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Text(
                                text = if (ehFavorito) "  Remover da Equipe" else "  Salvar na Equipe",
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
