package com.equipe.pokedex.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonListaResponse(
    val results: List<PokemonResumoDto> = emptyList()
)

@Serializable
data class PokemonResumoDto(
    val name: String,
    val url: String
) {
    val id: Int
        get() = url.trimEnd('/').substringAfterLast('/').toIntOrNull() ?: 0
}

@Serializable
data class PokemonDetalheResponse(
    val id: Int,
    val name: String,
    val height: Int = 0,
    val weight: Int = 0,
    val types: List<TipoSlotDto> = emptyList(),
    val stats: List<StatSlotDto> = emptyList()
)

@Serializable
data class TipoSlotDto(
    val slot: Int,
    val type: NomeUrlDto
)

@Serializable
data class StatSlotDto(
    @SerialName("base_stat") val baseStat: Int,
    val stat: NomeUrlDto
)

@Serializable
data class NomeUrlDto(
    val name: String
)

data class PokemonItem(
    val id: Int,
    val nome: String,
    val imagemUrl: String,
    val tipos: List<String> = emptyList(),
    val altura: Int = 0,
    val peso: Int = 0,
    val stats: List<Pair<String, Int>> = emptyList()
)
