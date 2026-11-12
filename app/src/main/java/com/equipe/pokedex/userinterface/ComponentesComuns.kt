package com.equipe.pokedex.userinterface

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.equipe.pokedex.data.PokemonItem

val VermelhoPokedex = Color(0xFFE61E25)
val CinzaFundoCard = Color(0xFFE5E9EC)

fun corPorTipo(tipo: String): Color = when (tipo.lowercase()) {
    "grass" -> Color(0xFF8CD872)
    "fire" -> Color(0xFFF35959)
    "water" -> Color(0xFF6BC5F8)
    "electric" -> Color(0xFFF6DC66)
    "poison" -> Color(0xFFB567CE)
    "normal" -> Color(0xFFD2C7DF)
    "bug" -> Color(0xFFA8B820)
    "flying" -> Color(0xFFA890F0)
    "ground" -> Color(0xFFE0C068)
    "fairy" -> Color(0xFFEE99AC)
    "fighting" -> Color(0xFFC03028)
    "psychic" -> Color(0xFFF85888)
    "rock" -> Color(0xFFB8A038)
    "ghost" -> Color(0xFF705898)
    "ice" -> Color(0xFF98D8D8)
    "dragon" -> Color(0xFF7038F8)
    else -> Color(0xFFB0BEC5)
}

@Composable
fun LogoPokebola(modifier: Modifier = Modifier, tamanho: Dp = 76.dp) {
    Canvas(modifier = modifier.size(tamanho)) {
        val stroke = size.minDimension * 0.08f
        val radius = (size.minDimension - stroke) / 2f
        val topLeft = Offset(stroke / 2f, stroke / 2f)
        val arcSize = Size(radius * 2f, radius * 2f)

        drawArc(
            color = VermelhoPokedex,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = topLeft,
            size = arcSize
        )
        drawArc(
            color = Color.White,
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = topLeft,
            size = arcSize
        )
        drawCircle(
            color = Color(0xFF2B2B2B),
            radius = radius,
            style = Stroke(width = stroke)
        )
        drawLine(
            color = Color(0xFF2B2B2B),
            start = Offset(stroke / 2f, center.y),
            end = Offset(size.width - stroke / 2f, center.y),
            strokeWidth = stroke
        )
        drawCircle(
            color = Color.White,
            radius = radius * 0.28f
        )
        drawCircle(
            color = Color(0xFF2B2B2B),
            radius = radius * 0.28f,
            style = Stroke(width = stroke * 0.85f)
        )
    }
}

@Composable
fun BarraSuperiorPokedex(titulo: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(VermelhoPokedex)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = titulo,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PokemonCard(
    pokemon: PokemonItem,
    ehFavorito: Boolean,
    onAlternarFavorito: () -> Unit,
    onAbrirDetalhe: () -> Unit
) {
    val corFundoTopo = corPorTipo(pokemon.tipos.firstOrNull() ?: "")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAbrirDetalhe() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CinzaFundoCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp)
                    .background(corFundoTopo)
            ) {
                AsyncImage(
                    model = pokemon.imagemUrl,
                    contentDescription = pokemon.nome,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(96.dp)
                        .align(Alignment.Center)
                )
                IconButton(
                    onClick = onAlternarFavorito,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (ehFavorito) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favoritar",
                        tint = if (ehFavorito) VermelhoPokedex else Color(0xFF555555)
                    )
                }
            }
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = "#%03d".format(pokemon.id),
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = pokemon.nome,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    pokemon.tipos.forEach { tipo ->
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(corPorTipo(tipo))
                                .padding(horizontal = 10.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = tipo,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}
