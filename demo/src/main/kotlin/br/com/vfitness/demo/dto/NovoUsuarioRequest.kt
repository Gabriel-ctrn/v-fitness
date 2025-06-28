package br.com.vfitness.demo.dto

import br.com.vfitness.demo.entity.Perfil

/**
 * DTO para cadastro de usuário, incluindo perfil (ADMIN ou ALUNO)
 */
data class NovoUsuarioRequest(
    val nome: String,
    val email: String,
    val senha: String,
    val nivelExperiencia: String = "",
    val perfil: Perfil = Perfil.ALUNO,
    val academiaId: Long
)
