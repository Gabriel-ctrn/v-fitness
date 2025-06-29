package br.com.vfitness.demo.dto

data class NovoExercicioRequest(
    val nome: String,
    val grupoMuscular: String,
    val tipo: String,
    val cargas: List<Double>,
    val series: Int,
    val repeticoes: List<String>,
    val treinoId: Long
)
