package com.equipe.pokedex.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonFavoritoDao {
    @Query("SELECT * FROM pokemons_favoritos WHERE usuarioId = :usuarioId ORDER BY pokemonId ASC")
    fun observarFavoritos(usuarioId: Int): Flow<List<PokemonFavorito>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(favorito: PokemonFavorito)

    @Delete
    suspend fun remover(favorito: PokemonFavorito)

    @Query("SELECT EXISTS(SELECT 1 FROM pokemons_favoritos WHERE usuarioId = :usuarioId AND pokemonId = :pokemonId)")
    suspend fun ehFavorito(usuarioId: Int, pokemonId: Int): Boolean
}
