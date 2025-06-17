package br.com.vfitness.demo.entity

import jakarta.persistence.*

@Entity
data class Maquina(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val nome: String,

    val grupoMuscular: String,

    @ManyToOne
    @JoinColumn(name = "academia_id")
    val academia: Academia
)