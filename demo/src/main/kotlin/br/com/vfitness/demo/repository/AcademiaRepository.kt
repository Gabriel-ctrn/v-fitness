package br.com.vfitness.demo.repository

import br.com.vfitness.demo.entity.Academia
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AcademiaRepository : JpaRepository<Academia, Long>