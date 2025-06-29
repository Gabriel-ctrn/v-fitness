package br.com.vfitness.demo.repository

import br.com.vfitness.demo.entity.PlanoTreino
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlanoTreinoRepository : JpaRepository<PlanoTreino, Long>
