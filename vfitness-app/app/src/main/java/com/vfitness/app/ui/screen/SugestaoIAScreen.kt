package com.vfitness.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vfitness.app.ui.theme.Primary
import com.vfitness.app.ui.theme.VFitnessTheme

@Composable
fun SugestaoIAScreen(
    sugestao: String,
    onVoltar: () -> Unit
) {
    VFitnessTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Sugestão da IA", style = MaterialTheme.typography.titleLarge, color = Primary)
                Spacer(modifier = Modifier.height(24.dp))
                Text(sugestao, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = onVoltar) {
                    Text("Voltar")
                }
            }
        }
    }
}
