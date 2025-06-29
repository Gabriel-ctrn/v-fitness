package br.com.vfitness.demo.repository

import br.com.vfitness.demo.entity.Exercicio
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExercicioRepository : JpaRepository<Exercicio, Long> {
    fun findAllByGrupoMuscular(grupoMuscular: String): List<Exercicio>
    fun findAllByTipo(tipo: String): List<Exercicio>
    fun findAllByTreinoId(treinoId: Long): List<Exercicio>
}