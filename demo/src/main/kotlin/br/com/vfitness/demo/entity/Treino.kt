package br.com.vfitness.demo.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.Duration
import java.time.LocalDateTime

@Entity
class Treino(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = 0,
    var nome: String,
    @ManyToOne @JoinColumn(name = "usuario_id")
    var usuario: Usuario,
    override var inicio: LocalDateTime? = null,
    override var fim: LocalDateTime? = null,
    @Transient var ultimoEvento: LocalDateTime? = null,
    @Transient private val _descansos: MutableList<Duration> = mutableListOf(),
    @OneToMany(mappedBy = "treino", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    private val _itens: List<ItemTreino> = emptyList(),
    @ManyToOne
    @JoinColumn(name = "plano_treino_id")
    var planoTreino: PlanoTreino? = null
) : Atividade(id, inicio, fim) {
    val itens: List<ItemTreino>
        get() = _itens
    val descansos: List<Duration>
        get() = _descansos

    val tempoTotal: String
        get() {
            val duracao = getDuracao()
            return if (duracao != Duration.ZERO) {
                String.format("%d min, %d seg", duracao.toMinutes(), duracao.toSecondsPart())
            } else "Em andamento"
        }

    @get:JsonIgnore
    val tempoTotalDescanso: String
        get() {
            val total = descansos.fold(Duration.ZERO, Duration::plus)
            return String.format("%d min, %d seg", total.toMinutes(), total.toSecondsPart())
        }

    // Polimorfismo: sobrescrevendo método da superclasse
    override fun getDuracao(): Duration {
        return if (inicio != null && fim != null) {
            Duration.between(inicio, fim)
        } else {
            Duration.ZERO
        }
    }
}
