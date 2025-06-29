package br.com.vfitness.demo.service

import br.com.vfitness.demo.entity.Usuario
import br.com.vfitness.demo.entity.Perfil
import br.com.vfitness.demo.repository.UsuarioRepository
import br.com.vfitness.demo.repository.AcademiaRepository
import br.com.vfitness.demo.dto.NovoUsuarioRequest
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UsuarioService(
    private val usuarioRepository: UsuarioRepository,
    private val academiaRepository: AcademiaRepository
) {

    fun listar(): List<Usuario> = usuarioRepository.findAll()

    fun porAcademia(academiaId: Long): List<Usuario> =
        usuarioRepository.findAllByAcademiaId(academiaId)

    fun buscarPorId(id: Long): ResponseEntity<Usuario> {
        val usuario = usuarioRepository.findById(id).orElse(null)
        return usuario?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    fun criar(usuario: Usuario): Usuario = usuarioRepository.save(usuario)

    fun criarComDto(request: br.com.vfitness.demo.dto.NovoUsuarioRequest): Usuario {
        val academia = academiaRepository.findById(request.academiaId).orElseThrow { RuntimeException("Academia não encontrada") }
        val usuario = Usuario(
            nome = request.nome,
            email = request.email,
            senha = request.senha,
            nivelExperiencia = request.nivelExperiencia,
            perfil = request.perfil,
            academia = academia
        )
        return usuarioRepository.save(usuario)
    }

    fun atualizar(id: Long, atualizada: Usuario): ResponseEntity<Usuario> {
        val existente = usuarioRepository.findById(id).orElse(null)
        return if (existente != null) {
            val novo = existente.copy(
                nome = atualizada.nome,
                email = atualizada.email,
                nivelExperiencia = atualizada.nivelExperiencia,
                perfil = atualizada.perfil,
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

    fun autenticar(email: String, senha: String): Usuario? {
        return usuarioRepository.findAll().find { it.email == email && it.senha == senha }
    }
}