package br.com.vfitness.demo.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
// Exercicio agora representa também o item do treino
// e está vinculado a um treino
// (remover a entidade ItemTreino depois)
data class Exercicio(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var nome: String,
    var grupoMuscular: String,
    var tipo: String,
    var carga: Double,
    var series: Int,
    var repeticoes: String,
    @ManyToOne
    @JoinColumn(name = "treino_id")
    @JsonIgnore
    val treino: Treino
)