package br.com.vfitness.demo.entity

import jakarta.persistence.*

@Entity
data class ItemTreino(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val exercicio: String,

    val carga: Double,

    val repeticoes: String,

    @ManyToOne
    @JoinColumn(name = "treino_id")
    val treino: Treino
)