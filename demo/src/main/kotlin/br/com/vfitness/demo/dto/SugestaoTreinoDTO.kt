package br.com.vfitness.demo.dto

data class ExercicioSugestaoDTO(
    val nome: String = "",
    val carga: String = "",
    val series: String = "",
    val repeticoes: String = ""
)

data class SugestaoTreinoDTO(
    val maquina: ExercicioSugestaoDTO = ExercicioSugestaoDTO(),
    val livre: ExercicioSugestaoDTO = ExercicioSugestaoDTO()
)
