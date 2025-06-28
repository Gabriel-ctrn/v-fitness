package com.vfitness.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vfitness.app.ui.theme.Primary
import com.vfitness.app.ui.theme.Secondary
import com.vfitness.app.ui.theme.VFitnessTheme

// Modelo simples para treino
 data class Treino(val id: Long, val nome: String, val itens: List<ItemTreino>)
 data class ItemTreino(val id: Long, val exercicio: String, val carga: Double, val repeticoes: String, val maquina: String)

@Composable
fun TreinosScreen(
    treinos: List<Treino>,
    onTreinoClick: (Treino) -> Unit
) {
    VFitnessTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text(
                    text = "Seus treinos",
                    style = MaterialTheme.typography.titleLarge,
                    color = Primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(treinos) { treino ->
                        TreinoItem(treino, onTreinoClick)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TreinoItem(treino: Treino, onClick: (Treino) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(treino) },
        colors = CardDefaults.cardColors(containerColor = Secondary)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(treino.nome, style = MaterialTheme.typography.titleLarge, color = Color.White)
            Text("${treino.itens.size} exercícios", style = MaterialTheme.typography.bodyLarge, color = Color.White)
        }
    }
}
