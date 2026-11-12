package com.equipe.pokedex.userinterface

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CatchingPokemon
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.equipe.pokedex.viewmodel.PokedexViewModel

@Composable
fun AppNavegacao(pokedexViewModel: PokedexViewModel = viewModel()) {
    val authState by pokedexViewModel.authState.collectAsState()
    val listaState by pokedexViewModel.listaState.collectAsState()
    val detalheState by pokedexViewModel.detalheState.collectAsState()
    val favoritos by pokedexViewModel.favoritos.collectAsState()
    val busca by pokedexViewModel.busca.collectAsState()

    val favoritosIds = favoritos.map { it.pokemonId }.toSet()

    if (authState.carregandoSessao) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = VermelhoPokedex)
        }
        return
    }

    val navController = rememberNavController()
    val usuarioLogado = authState.usuarioLogado

    if (usuarioLogado == null) {
        NavHost(navController = navController, startDestination = RotaLogin) {
            composable<RotaLogin> {
                TelaLogin(
                    erro = authState.erroAuth,
                    onEntrar = { email, senha -> pokedexViewModel.entrar(email, senha) },
                    onIrParaCadastro = {
                        pokedexViewModel.limparErroAuth()
                        navController.navigate(RotaCadastro)
                    }
                )
            }
            composable<RotaCadastro> {
                TelaCadastro(
                    erro = authState.erroAuth,
                    onCadastrar = { nome, email, senha, conf ->
                        pokedexViewModel.cadastrar(nome, email, senha, conf)
                    },
                    onIrParaLogin = {
                        pokedexViewModel.limparErroAuth()
                        navController.popBackStack()
                    }
                )
            }
        }
    } else {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val destinoAtual = backStackEntry?.destination
        val mostrarBarraInferior = destinoAtual?.hasRoute<RotaDetalhe>() != true

        Scaffold(
            bottomBar = {
                if (mostrarBarraInferior) {
                    NavigationBar(containerColor = Color.White) {
                        val itens = listOf(
                            Triple(RotaExplorar, "Explorar", Icons.Default.Explore),
                            Triple(RotaEquipe, "Equipe", Icons.Default.CatchingPokemon),
                            Triple(RotaPerfil, "Perfil", Icons.Default.Person)
                        )
                        itens.forEach { (rota, titulo, icone) ->
                            val selecionado = when (rota) {
                                RotaExplorar -> destinoAtual?.hasRoute<RotaExplorar>() == true
                                RotaEquipe -> destinoAtual?.hasRoute<RotaEquipe>() == true
                                RotaPerfil -> destinoAtual?.hasRoute<RotaPerfil>() == true
                                else -> false
                            }
                            NavigationBarItem(
                                selected = selecionado,
                                onClick = {
                                    navController.navigate(rota) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Icon(icone, contentDescription = titulo) },
                                label = { Text(titulo) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = VermelhoPokedex,
                                    selectedTextColor = VermelhoPokedex,
                                    indicatorColor = Color.Transparent
                                )
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = RotaExplorar,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable<RotaExplorar> {
                    TelaExplorar(
                        state = listaState,
                        busca = busca,
                        favoritosIds = favoritosIds,
                        onBuscaChange = { pokedexViewModel.atualizarBusca(it) },
                        onTentarNovamente = { pokedexViewModel.carregarPokemons() },
                        onAlternarFavorito = { pokedexViewModel.alternarFavorito(it) },
                        onAbrirDetalhe = { id -> navController.navigate(RotaDetalhe(id)) }
                    )
                }
                composable<RotaEquipe> {
                    TelaEquipe(
                        favoritos = favoritos,
                        onIrParaExplorar = {
                            navController.navigate(RotaExplorar) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onRemoverFavorito = { pokedexViewModel.alternarFavorito(it) },
                        onAbrirDetalhe = { id -> navController.navigate(RotaDetalhe(id)) }
                    )
                }
                composable<RotaPerfil> {
                    TelaPerfil(
                        usuario = usuarioLogado,
                        onSair = { pokedexViewModel.sair() }
                    )
                }
                composable<RotaDetalhe> { entry ->
                    val rota = entry.toRoute<RotaDetalhe>()
                    TelaDetalhe(
                        pokemonId = rota.pokemonId,
                        state = detalheState,
                        ehFavorito = favoritosIds.contains(rota.pokemonId),
                        onCarregar = { pokedexViewModel.carregarDetalhe(it) },
                        onAlternarFavorito = { pokedexViewModel.alternarFavorito(it) },
                        onVoltar = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
