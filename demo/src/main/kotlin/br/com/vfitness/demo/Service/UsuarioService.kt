package br.com.vfitness.demo.service

import br.com.vfitness.demo.entity.Usuario
import br.com.vfitness.demo.repository.UsuarioRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UsuarioService(private val usuarioRepository: UsuarioRepository) {

    fun listar(): List<Usuario> = usuarioRepository.findAll()

    fun porAcademia(academiaId: Long): List<Usuario> =
        usuarioRepository.findAllByAcademiaId(academiaId)

    fun buscarPorId(id: Long): ResponseEntity<Usuario> {
        val usuario = usuarioRepository.findById(id).orElse(null)
        return usuario?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    fun criar(usuario: Usuario): Usuario = usuarioRepository.save(usuario)

    fun autenticar(email: String, senha: String): Usuario? {
        val usuario = usuarioRepository.findByEmail(email).orElse(null)

        return if (usuario != null && usuario.senha == senha) {
            usuario
        } else {
            null
        }
    }

    fun atualizar(id: Long, atualizada: Usuario): ResponseEntity<Usuario> {
        val existente = usuarioRepository.findById(id).orElse(null)
        return if (existente != null) {
            val novo = existente.copy(
                nome = atualizada.nome,
                email = atualizada.email,
                nivelExperiencia = atualizada.nivelExperiencia,
                academia = atualizada.academia
            )
            ResponseEntity.ok(usuarioRepository.save(novo))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    fun deletar(id: Long): ResponseEntity<Void> {
        val existente = usuarioRepository.findById(id).orElse(null)
        return if (existente != null) {
            usuarioRepository.delete(existente)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}