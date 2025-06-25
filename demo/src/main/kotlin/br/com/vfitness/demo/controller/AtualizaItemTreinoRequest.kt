package br.com.vfitness.demo.controller

data class AtualizaItemTreinoRequest(
    val exercicio: String,
    val carga: Double,
    val repeticoes: String,
    val treinoId: Long
)
