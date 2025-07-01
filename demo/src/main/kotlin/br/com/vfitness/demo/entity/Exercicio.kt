package br.com.vfitness.demo.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
 
 
 
data class Exercicio(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var nome: String,
    var grupoMuscular: String,
    var tipo: String,
    var cargas: List<Double>,
    var repeticoes: List<String>,
    var series: Int,
    @ManyToOne
    @JoinColumn(name = "treino_id")
    @JsonIgnore
    val treino: Treino?
)