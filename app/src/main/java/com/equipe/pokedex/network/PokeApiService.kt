package com.equipe.pokedex.network

import com.equipe.pokedex.data.PokemonDetalheResponse
import com.equipe.pokedex.data.PokemonListaResponse
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {
    @GET("pokemon")
    suspend fun listarPokemons(
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0
    ): PokemonListaResponse

    @GET("pokemon/{idOuNome}")
    suspend fun buscarPokemon(
        @Path("idOuNome") idOuNome: String
    ): PokemonDetalheResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    val api: PokeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(PokeApiService::class.java)
    }
}
