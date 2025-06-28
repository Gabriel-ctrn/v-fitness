package com.vfitness.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vfitness.app.ui.theme.Primary
import com.vfitness.app.ui.theme.Secondary
import com.vfitness.app.ui.theme.Accent
import com.vfitness.app.ui.theme.VFitnessTheme

@Composable
fun TreinoDoDiaScreen(
    treino: Treino,
    maquinasOcupadas: List<String>,
    onToggleMaquina: (String) -> Unit,
    onSugestaoIA: () -> Unit
) {
    VFitnessTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text("Treino do Dia: ${treino.nome}", style = MaterialTheme.typography.titleLarge, color = Primary)
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(treino.itens) { item ->
                        ItemTreinoRow(item, maquinasOcupadas, onToggleMaquina)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onSugestaoIA,
                    colors = ButtonDefaults.buttonColors(containerColor = Accent),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pedir sugestão da IA", color = Primary)
                }
            }
        }
    }
}

@Composable
fun ItemTreinoRow(
    item: ItemTreino,
    maquinasOcupadas: List<String>,
    onToggleMaquina: (String) -> Unit
) {
    val ocupada = maquinasOcupadas.contains(item.maquina)
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = if (ocupada) Accent else Secondary)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.exercicio, style = MaterialTheme.typography.titleLarge, color = Color.White)
                Text("Carga: ${item.carga} | Reps: ${item.repeticoes}", color = Color.White)
                Text("Máquina: ${item.maquina}", color = Color.White)
            }
            Checkbox(
                checked = ocupada,
                onCheckedChange = { onToggleMaquina(item.maquina) },
                colors = CheckboxDefaults.colors(checkedColor = Primary)
            )
            Text("Ocupada", color = Color.White, modifier = Modifier.padding(start = 8.dp))
        }
    }
}
