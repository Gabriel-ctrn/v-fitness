package br.com.vfitness.demo.repository

import br.com.vfitness.demo.entity.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UsuarioRepository : JpaRepository<Usuario, Long> {
    fun findAllByAcademiaId(academiaId: Long): List<Usuario>
}
