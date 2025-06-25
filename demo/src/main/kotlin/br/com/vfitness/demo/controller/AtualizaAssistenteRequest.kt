package br.com.vfitness.demo.controller

data class AtualizaAssistenteRequest(
    val nome: String,
    val email: String,
    val especialidade: String,
    val academiaId: Long
)
