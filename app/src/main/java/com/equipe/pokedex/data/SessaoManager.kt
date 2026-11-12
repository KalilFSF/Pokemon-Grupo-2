package com.equipe.pokedex.data

import android.content.Context

class SessaoManager(context: Context) {
    private val prefs = context.getSharedPreferences("pokedex_sessao", Context.MODE_PRIVATE)

    fun salvarUsuarioLogado(usuarioId: Int) {
        prefs.edit().putInt("usuario_id", usuarioId).apply()
    }

    fun obterUsuarioLogadoId(): Int {
        return prefs.getInt("usuario_id", -1)
    }

    fun limparSessao() {
        prefs.edit().remove("usuario_id").apply()
    }
}
