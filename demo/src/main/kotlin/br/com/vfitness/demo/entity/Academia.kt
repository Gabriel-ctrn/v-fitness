package br.com.vfitness.demo.entity

import jakarta.persistence.*

@Entity
data class Academia(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val nome: String = "",

    val endereco: String = ""
)