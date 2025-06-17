package br.com.vfitness.demo.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.Duration
import java.time.LocalDateTime

@Entity
data class Treino(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val nome: String,

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    val usuario: Usuario,

    // controle dos tempos
    var inicio: LocalDateTime? = null,
    var fim: LocalDateTime? = null,

    @Transient
    var ultimoEvento: LocalDateTime? = null,

    @Transient
    val descansos: MutableList<Duration> = mutableListOf(),

    @OneToMany(mappedBy = "treino", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonIgnore
    val itens: List<ItemTreino> = emptyList()
) {
    // tempo total de treino
    val tempoTotal: String
        get() {
            return if (inicio != null && fim != null) {
                val duracao = Duration.between(inicio, fim)
                "${duracao.toMinutes()} minutos e ${duracao.toSecondsPart()} segundos"
            } else "Treino não finalizado"
        }

    // tempo total de descanso
    val tempoTotalDescanso: String
        get() {
            val total = descansos.fold(Duration.ZERO, Duration::plus)
            return "${total.toMinutes()} minutos e ${total.toSecondsPart()} segundos"
        }
}