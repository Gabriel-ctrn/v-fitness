package br.com.vfitness.demo.entity

import jakarta.persistence.*

@Entity
data class PlanoTreino(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val nome: String,
    val objetivo: String,
    @ManyToOne val usuario: Usuario,
    @OneToMany(mappedBy = "planoTreino", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val treinos: List<Treino> = emptyList()
)
