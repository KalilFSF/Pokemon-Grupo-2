package com.equipe.pokedex.viewmodel

import com.equipe.pokedex.data.PokemonItem
import com.equipe.pokedex.data.Usuario

sealed interface ListaUiState {
    data object Carregando : ListaUiState
    data class Sucesso(val pokemons: List<PokemonItem>) : ListaUiState
    data class Erro(val mensagem: String) : ListaUiState
}

sealed interface DetalheUiState {
    data object Carregando : DetalheUiState
    data class Sucesso(val pokemon: PokemonItem) : DetalheUiState
    data class Erro(val mensagem: String) : DetalheUiState
}

data class AuthUiState(
    val carregandoSessao: Boolean = true,
    val usuarioLogado: Usuario? = null,
    val erroAuth: String? = null
)
