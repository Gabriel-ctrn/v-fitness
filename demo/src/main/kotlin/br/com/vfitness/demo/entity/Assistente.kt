package br.com.vfitness.demo.entity

import jakarta.persistence.*

@Entity
data class Assistente(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val identificador: String, // Ex: "Copilot", "DeepSeek"

    @Column(nullable = false)
    val modelo: String,        // Ex: "GPT-4", "DeepSeek-Coder"

    @ManyToOne
    val academia: Academia
)
