package br.com.vfitness.demo.entity

import java.time.Duration

// Interface para entidades que possuem duração
interface TemDuracao {
    fun getDuracao(): Duration
}

// Classe abstrata para hierarquia de atividades
abstract class Atividade(
    open val id: Long = 0,
    open var inicio: java.time.LocalDateTime? = null,
    open var fim: java.time.LocalDateTime? = null
) : TemDuracao {
    // Método polimórfico
    override fun getDuracao(): Duration {
        return if (inicio != null && fim != null) {
            Duration.between(inicio, fim)
        } else {
            Duration.ZERO
        }
    }
}
