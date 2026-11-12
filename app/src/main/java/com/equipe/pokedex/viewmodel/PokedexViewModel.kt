package com.equipe.pokedex.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.equipe.pokedex.data.AppDatabase
import com.equipe.pokedex.data.PokedexRepository
import com.equipe.pokedex.data.PokemonFavorito
import com.equipe.pokedex.data.PokemonItem
import com.equipe.pokedex.data.SessaoManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PokedexViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PokedexRepository

    init {
        val db = AppDatabase.getInstance(application)
        val sessao = SessaoManager(application)
        repository = PokedexRepository(
            usuarioDao = db.usuarioDao(),
            favoritoDao = db.pokemonFavoritoDao(),
            sessaoManager = sessao
        )
        verificarSessao()
    }

    private val _authState = MutableStateFlow(AuthUiState())
    val authState: StateFlow<AuthUiState> = _authState.asStateFlow()

    private val _listaState = MutableStateFlow<ListaUiState>(ListaUiState.Carregando)
    val listaState: StateFlow<ListaUiState> = _listaState.asStateFlow()

    private val _detalheState = MutableStateFlow<DetalheUiState>(DetalheUiState.Carregando)
    val detalheState: StateFlow<DetalheUiState> = _detalheState.asStateFlow()

    private val _favoritos = MutableStateFlow<List<PokemonFavorito>>(emptyList())
    val favoritos: StateFlow<List<PokemonFavorito>> = _favoritos.asStateFlow()

    private val _busca = MutableStateFlow("")
    val busca: StateFlow<String> = _busca.asStateFlow()

    private var favoritosJob: Job? = null

    private fun verificarSessao() {
        viewModelScope.launch {
            val usuario = repository.obterUsuarioSessao()
            _authState.update { it.copy(carregandoSessao = false, usuarioLogado = usuario) }
            if (usuario != null) {
                observarFavoritosDoUsuario(usuario.id)
                carregarPokemons()
            }
        }
    }

    fun limparErroAuth() {
        _authState.update { it.copy(erroAuth = null) }
    }

    fun entrar(email: String, senha: String) {
        viewModelScope.launch {
            val resultado = repository.entrar(email, senha)
            resultado.fold(
                onSuccess = { usuario ->
                    _authState.update { it.copy(usuarioLogado = usuario, erroAuth = null) }
                    observarFavoritosDoUsuario(usuario.id)
                    carregarPokemons()
                },
                onFailure = { erro ->
                    _authState.update { it.copy(erroAuth = erro.message) }
                }
            )
        }
    }

    fun cadastrar(nome: String, email: String, senha: String, confirmarSenha: String) {
        viewModelScope.launch {
            val resultado = repository.cadastrar(nome, email, senha, confirmarSenha)
            resultado.fold(
                onSuccess = { usuario ->
                    _authState.update { it.copy(usuarioLogado = usuario, erroAuth = null) }
                    observarFavoritosDoUsuario(usuario.id)
                    carregarPokemons()
                },
                onFailure = { erro ->
                    _authState.update { it.copy(erroAuth = erro.message) }
                }
            )
        }
    }

    fun sair() {
        repository.sair()
        favoritosJob?.cancel()
        _favoritos.value = emptyList()
        _busca.value = ""
        _authState.update { it.copy(usuarioLogado = null, erroAuth = null) }
    }

    private fun observarFavoritosDoUsuario(usuarioId: Int) {
        favoritosJob?.cancel()
        favoritosJob = viewModelScope.launch {
            repository.observarFavoritos(usuarioId).collect { lista ->
                _favoritos.value = lista
            }
        }
    }

    fun atualizarBusca(texto: String) {
        _busca.value = texto
    }

    fun carregarPokemons() {
        viewModelScope.launch {
            _listaState.value = ListaUiState.Carregando
            try {
                val pokemons = repository.carregarPokemons()
                _listaState.value = ListaUiState.Sucesso(pokemons)
            } catch (e: Exception) {
                _listaState.value = ListaUiState.Erro(
                    "Falha ao carregar Pokémons. Verifique sua conexão."
                )
            }
        }
    }

    fun carregarDetalhe(pokemonId: Int) {
        viewModelScope.launch {
            _detalheState.value = DetalheUiState.Carregando
            try {
                val pokemon = repository.buscarPokemonDetalhe(pokemonId)
                _detalheState.value = DetalheUiState.Sucesso(pokemon)
            } catch (e: Exception) {
                _detalheState.value = DetalheUiState.Erro(
                    "Erro ao carregar detalhes do Pokémon."
                )
            }
        }
    }

    fun alternarFavorito(pokemon: PokemonItem) {
        val usuarioId = _authState.value.usuarioLogado?.id ?: return
        viewModelScope.launch {
            repository.alternarFavorito(usuarioId, pokemon)
        }
    }
}
