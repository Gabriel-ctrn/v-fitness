package br.com.vfitness.demo.dto

data class NovoItemTreinoRequest(
    val exercicio: String,
    val carga: Double,
    val repeticoes: String,
    val treinoId: Long
)
