package br.com.vfitness.demo.repository

import br.com.vfitness.demo.entity.ItemTreino
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.transaction.annotation.Transactional

interface ItemTreinoRepository : JpaRepository<ItemTreino, Long> {
    fun findAllByTreinoId(treinoId: Long): List<ItemTreino>

    @Transactional
    @Modifying
    fun deleteAllByTreinoId(treinoId: Long)
}
