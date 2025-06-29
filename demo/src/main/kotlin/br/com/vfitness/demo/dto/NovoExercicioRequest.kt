package br.com.vfitness.demo.dto

data class NovoExercicioRequest(
    val nome: String,
    val grupoMuscular: String,
    val tipo: String,
    val carga: Double,
    val series: Int,
    val repeticoes: String,
    val treinoId: Long
)
