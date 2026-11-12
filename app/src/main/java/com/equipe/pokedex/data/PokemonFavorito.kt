package com.equipe.pokedex.data

import androidx.room.Entity

@Entity(
    tableName = "pokemons_favoritos",
    primaryKeys = ["usuarioId", "pokemonId"]
)
data class PokemonFavorito(
    val usuarioId: Int,
    val pokemonId: Int,
    val nome: String,
    val imagemUrl: String,
    val tipos: String
)
