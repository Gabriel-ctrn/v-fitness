package br.com.vfitness.demo.repository

import br.com.vfitness.demo.entity.Assistente
import org.springframework.data.jpa.repository.JpaRepository

interface AssistenteRepository : JpaRepository<Assistente, Long>
