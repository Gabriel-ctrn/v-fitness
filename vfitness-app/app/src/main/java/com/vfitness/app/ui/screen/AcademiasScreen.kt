package com.vfitness.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vfitness.app.ui.theme.Primary
import com.vfitness.app.ui.theme.Secondary
import com.vfitness.app.ui.theme.VFitnessTheme

// Modelo simples para academia
 data class Academia(val id: Long, val nome: String, val endereco: String)

@Composable
fun AcademiasScreen(
    academias: List<Academia>,
    onAcademiaClick: (Academia) -> Unit
) {
    VFitnessTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text(
                    text = "Escolha uma academia",
                    style = MaterialTheme.typography.titleLarge,
                    color = Primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(academias) { academia ->
                        AcademiaItem(academia, onAcademiaClick)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun AcademiaItem(academia: Academia, onClick: (Academia) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(academia) },
        colors = CardDefaults.cardColors(containerColor = Secondary)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(academia.nome, style = MaterialTheme.typography.titleLarge, color = Color.White)
            Text(academia.endereco, style = MaterialTheme.typography.bodyLarge, color = Color.White)
        }
    }
}
