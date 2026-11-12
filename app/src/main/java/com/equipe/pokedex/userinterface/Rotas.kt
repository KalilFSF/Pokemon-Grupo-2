package com.equipe.pokedex.userinterface

import kotlinx.serialization.Serializable

@Serializable
object RotaLogin

@Serializable
object RotaCadastro

@Serializable
object RotaExplorar

@Serializable
object RotaEquipe

@Serializable
object RotaPerfil

@Serializable
data class RotaDetalhe(val pokemonId: Int)
