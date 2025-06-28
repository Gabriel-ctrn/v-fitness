package br.com.vfitness.demo.dto

data class AtualizaAssistenteRequest(
    val identificador: String, // Ex: "Copilot", "DeepSeek"
    val modelo: String,        // Ex: "GPT-4", "DeepSeek-Coder"
    val academiaId: Long
)
