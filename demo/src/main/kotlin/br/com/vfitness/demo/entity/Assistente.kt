package br.com.vfitness.demo.entity

import jakarta.persistence.*

@Entity
data class Assistente(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val nome: String,

    @Column(nullable = false)
    val especialidade: String,

    @ManyToOne
    val academia: Academia
)
