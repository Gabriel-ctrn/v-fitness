package br.com.vfitness.demo.repository

import br.com.vfitness.demo.entity.ItemTreino
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemTreinoRepository : JpaRepository<ItemTreino, Long> {
    fun findAllByTreinoId(treinoId: Long): List<ItemTreino>
}
