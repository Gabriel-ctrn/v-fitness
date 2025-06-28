package br.com.vfitness.demo.dto

data class SugestaoTreinoDTO(
    val sugestoes: List<ItemTreinoDTO>,
    val aumentoCarga: Map<Long, Double> // exercicioId -> novo valor sugerido de carga
)
