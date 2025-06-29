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
    @ManyToOne @JoinColumn(name = "usuario_id")
    val usuario: Usuario,
    var inicio: LocalDateTime? = null,
    var fim: LocalDateTime? = null,
    @Transient var ultimoEvento: LocalDateTime? = null,
    @Transient val descansos: MutableList<Duration> = mutableListOf(),

    // CORREÇÃO: FetchType.EAGER para sempre carregar os itens do banco.
    @OneToMany(mappedBy = "treino", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val itens: List<ItemTreino> = emptyList(),

    @ManyToOne
    @JoinColumn(name = "plano_treino_id")
    val planoTreino: PlanoTreino? = null
) {
    val tempoTotal: String
        get() {
            return if (inicio != null && fim != null) {
                val duracao = Duration.between(inicio, fim)
                String.format("%d min, %d seg", duracao.toMinutes(), duracao.toSecondsPart())
            } else "Em andamento"
        }

    @get:JsonIgnore
    val tempoTotalDescanso: String
        get() {
            val total = descansos.fold(Duration.ZERO, Duration::plus)
            return String.format("%d min, %d seg", total.toMinutes(), total.toSecondsPart())
        }
}
