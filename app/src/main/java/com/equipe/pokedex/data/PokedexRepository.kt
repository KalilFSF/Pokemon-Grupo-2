package com.equipe.pokedex.data

import android.util.Patterns
import com.equipe.pokedex.network.PokeApiService
import com.equipe.pokedex.network.RetrofitClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest

fun String.sha256(): String =
    MessageDigest.getInstance("SHA-256")
        .digest(toByteArray())
        .joinToString("") { "%02x".format(it) }

class PokedexRepository(
    private val usuarioDao: UsuarioDao,
    private val favoritoDao: PokemonFavoritoDao,
    private val sessaoManager: SessaoManager,
    private val api: PokeApiService = RetrofitClient.api
) {
    // --- Autenticação e Sessão ---
    suspend fun cadastrar(nome: String, email: String, senha: String, confirmarSenha: String): Result<Usuario> {
        val nomeLimpo = nome.trim()
        val emailLimpo = email.trim().lowercase()

        if (nomeLimpo.isEmpty()) {
            return Result.failure(Exception("Informe seu nome completo."))
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailLimpo).matches()) {
            return Result.failure(Exception("Informe um e-mail válido."))
        }
        if (senha.length < 6) {
            return Result.failure(Exception("A senha deve ter 6 ou mais caracteres."))
        }
        if (senha != confirmarSenha) {
            return Result.failure(Exception("As senhas não conferem."))
        }
        if (usuarioDao.buscarPorEmail(emailLimpo) != null) {
            return Result.failure(Exception("Este e-mail já está cadastrado."))
        }

        val novoUsuario = Usuario(
            nome = nomeLimpo,
            email = emailLimpo,
            senhaHash = senha.sha256()
        )
        val idGerado = usuarioDao.inserir(novoUsuario).toInt()
        val usuarioSalvo = novoUsuario.copy(id = idGerado)
        sessaoManager.salvarUsuarioLogado(idGerado)
        return Result.success(usuarioSalvo)
    }

    suspend fun entrar(email: String, senha: String): Result<Usuario> {
        val emailLimpo = email.trim().lowercase()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailLimpo).matches() || senha.isEmpty()) {
            return Result.failure(Exception("E-mail ou senha inválidos."))
        }
        val usuario = usuarioDao.buscarPorEmail(emailLimpo)
        if (usuario == null || usuario.senhaHash != senha.sha256()) {
            return Result.failure(Exception("E-mail ou senha inválidos."))
        }
        sessaoManager.salvarUsuarioLogado(usuario.id)
        return Result.success(usuario)
    }

    suspend fun obterUsuarioSessao(): Usuario? {
        val id = sessaoManager.obterUsuarioLogadoId()
        if (id == -1) return null
        return usuarioDao.buscarPorId(id)
    }

    fun sair() {
        sessaoManager.limparSessao()
    }

    // --- API REST (PokéAPI) ---
    suspend fun carregarPokemons(): List<PokemonItem> = coroutineScope {
        val listaDto = api.listarPokemons(limit = 40, offset = 0).results
        listaDto.map { resumo ->
            async {
                val detalhe = api.buscarPokemon(resumo.id.toString())
                detalhe.toPokemonItem()
            }
        }.awaitAll()
    }

    suspend fun buscarPokemonDetalhe(pokemonId: Int): PokemonItem {
        return api.buscarPokemon(pokemonId.toString()).toPokemonItem()
    }

    private fun PokemonDetalheResponse.toPokemonItem(): PokemonItem {
        val urlImagem = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
        return PokemonItem(
            id = id,
            nome = name.replaceFirstChar { it.uppercase() },
            imagemUrl = urlImagem,
            tipos = types.sortedBy { it.slot }.map { it.type.name.replaceFirstChar { c -> c.uppercase() } },
            altura = height,
            peso = weight,
            stats = stats.map { it.stat.name to it.baseStat }
        )
    }

    // --- Persistência Local (Room) ---
    fun observarFavoritos(usuarioId: Int): Flow<List<PokemonFavorito>> {
        return favoritoDao.observarFavoritos(usuarioId)
    }

    suspend fun alternarFavorito(usuarioId: Int, pokemon: PokemonItem) {
        val favorito = PokemonFavorito(
            usuarioId = usuarioId,
            pokemonId = pokemon.id,
            nome = pokemon.nome,
            imagemUrl = pokemon.imagemUrl,
            tipos = pokemon.tipos.joinToString(",")
        )
        if (favoritoDao.ehFavorito(usuarioId, pokemon.id)) {
            favoritoDao.remover(favorito)
        } else {
            favoritoDao.inserir(favorito)
        }
    }
}
