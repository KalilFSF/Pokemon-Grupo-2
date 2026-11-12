package com.equipe.pokedex.userinterface

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.equipe.pokedex.data.PokemonFavorito
import com.equipe.pokedex.data.PokemonItem

@Composable
fun TelaEquipe(
    favoritos: List<PokemonFavorito>,
    onIrParaExplorar: () -> Unit,
    onRemoverFavorito: (PokemonItem) -> Unit,
    onAbrirDetalhe: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        BarraSuperiorPokedex("Minha Equipe")

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(215.dp)
                        .clickable { onIrParaExplorar() },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CinzaFundoCard)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Explore,
                            contentDescription = "Explorar",
                            tint = Color(0xFF6E6E6E),
                            modifier = Modifier.size(72.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Explorar +",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF6E6E6E)
                        )
                    }
                }
            }

            items(favoritos, key = { it.pokemonId }) { fav ->
                val item = PokemonItem(
                    id = fav.pokemonId,
                    nome = fav.nome,
                    imagemUrl = fav.imagemUrl,
                    tipos = fav.tipos.split(",").filter { it.isNotBlank() }
                )
                PokemonCard(
                    pokemon = item,
                    ehFavorito = true,
                    onAlternarFavorito = { onRemoverFavorito(item) },
                    onAbrirDetalhe = { onAbrirDetalhe(fav.pokemonId) }
                )
            }
        }
    }
}
