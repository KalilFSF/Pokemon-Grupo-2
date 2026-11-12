package com.equipe.pokedex.userinterface

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.equipe.pokedex.data.PokemonItem
import com.equipe.pokedex.viewmodel.ListaUiState

@Composable
fun TelaExplorar(
    state: ListaUiState,
    busca: String,
    favoritosIds: Set<Int>,
    onBuscaChange: (String) -> Unit,
    onTentarNovamente: () -> Unit,
    onAlternarFavorito: (PokemonItem) -> Unit,
    onAbrirDetalhe: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        BarraSuperiorPokedex("Explorar Pokédex")

        OutlinedTextField(
            value = busca,
            onValueChange = onBuscaChange,
            placeholder = { Text("Buscar Pokémon...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = CinzaFundoCard,
                focusedContainerColor = CinzaFundoCard
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )

        when (state) {
            is ListaUiState.Carregando -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = VermelhoPokedex)
                }
            }
            is ListaUiState.Erro -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = state.mensagem, color = VermelhoPokedex)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onTentarNovamente,
                        colors = ButtonDefaults.buttonColors(containerColor = VermelhoPokedex)
                    ) {
                        Text("Tentar novamente", color = Color.White)
                    }
                }
            }
            is ListaUiState.Sucesso -> {
                val filtrados = state.pokemons.filter {
                    it.nome.contains(busca, ignoreCase = true) ||
                        it.id.toString() == busca.trim()
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filtrados, key = { it.id }) { pokemon ->
                        PokemonCard(
                            pokemon = pokemon,
                            ehFavorito = favoritosIds.contains(pokemon.id),
                            onAlternarFavorito = { onAlternarFavorito(pokemon) },
                            onAbrirDetalhe = { onAbrirDetalhe(pokemon.id) }
                        )
                    }
                }
            }
        }
    }
}
