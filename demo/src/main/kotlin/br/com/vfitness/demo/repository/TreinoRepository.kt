package br.com.vfitness.demo.repository

import br.com.vfitness.demo.entity.Treino
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TreinoRepository : JpaRepository<Treino, Long> {
    fun findAllByUsuarioId(usuarioId: Long): List<Treino>
}
