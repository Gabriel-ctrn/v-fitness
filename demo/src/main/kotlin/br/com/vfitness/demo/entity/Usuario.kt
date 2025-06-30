package br.com.vfitness.demo.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

enum class Perfil {
    ADMIN, ALUNO
}

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

    @Enumerated(EnumType.STRING)
    val perfil: Perfil = Perfil.ALUNO,

    @OneToMany(mappedBy = "usuario", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonIgnore
    val treinos: List<Treino> = emptyList()
)