package br.com.vfitness.demo.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
data class Usuario(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val nome: String = "",

    @Column(unique = true)
    val email: String = "",

    val senha: String = "",

    val nivelExperiencia: String = "",

    @ManyToOne
    @JoinColumn(name = "academia_id")
    val academia: Academia = Academia(),

    @OneToMany(mappedBy = "usuario", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonIgnore
    val treinos: List<Treino> = emptyList()
)