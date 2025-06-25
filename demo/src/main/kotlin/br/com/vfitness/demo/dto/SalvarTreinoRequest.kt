package br.com.vfitness.demo.dto

data class SalvarTreinoRequest(
    val treinoId: Long,
    val itens: List<ItemTreinoDTO>
)