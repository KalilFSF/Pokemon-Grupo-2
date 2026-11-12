package com.equipe.pokedex.userinterface

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.equipe.pokedex.data.Usuario

@Composable
fun TelaPerfil(
    usuario: Usuario,
    onSair: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        BarraSuperiorPokedex("Perfil")

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = CinzaFundoCard),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Column {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Usuário: ${usuario.nome}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Email: ${usuario.email}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Button(
                    onClick = onSair,
                    shape = RoundedCornerShape(bottomStart = 14.dp, bottomEnd = 14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VermelhoPokedex),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = "SAIR DA CONTA",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}
