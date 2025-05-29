package br.com.vfitness.demo.repository

import br.com.vfitness.demo.entity.Maquina
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MaquinaRepository : JpaRepository<Maquina, Long> {
    fun findAllByAcademiaId(academiaId: Long): List<Maquina>
    fun findAllByGrupoMuscular(grupoMuscular: String): List<Maquina>
}