package br.com.vfitness.demo.dto

data class AtualizaItemTreinoRequest(
    val exercicio: String,
    val carga: Double,
    val repeticoes: String,
    val treinoId: Long
)
