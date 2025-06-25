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

    @OneToMany(mappedBy = "treino", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    // @JsonIgnore // <- Esta anotação foi removida para que os itens sejam enviados na resposta da API
    val itens: List<ItemTreino> = emptyList()
) {
    // tempo total de treino
    val tempoTotal: String
        get() {
            return if (inicio != null && fim != null) {
                val duracao = Duration.between(inicio, fim)
                // Formatando para minutos e segundos
                String.format("%d minutos e %d segundos", duracao.toMinutes(), duracao.seconds % 60)
            } else "Treino não finalizado"
        }

    // tempo total de descanso
    @get:JsonIgnore
    val tempoTotalDescanso: String
        get() {
            val total = descansos.fold(Duration.ZERO, Duration::plus)
            return String.format("%d minutos e %d segundos", total.toMinutes(), total.seconds % 60)
        }
}