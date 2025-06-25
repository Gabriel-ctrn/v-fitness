package br.com.vfitness.demo.controller

data class NovoAssistenteRequest(
    val nome: String,
    val email: String,
    val especialidade: String,
    val academiaId: Long
)
