package br.com.vfitness.demo.dto

data class NovoPlanoTreinoRequest(
    val nome: String,
    val objetivo: String,
    val usuarioId: Long
)
