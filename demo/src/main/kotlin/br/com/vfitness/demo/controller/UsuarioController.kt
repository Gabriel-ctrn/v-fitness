package br.com.vfitness.demo.controller

import br.com.vfitness.demo.entity.Usuario
import br.com.vfitness.demo.repository.UsuarioRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuarios")
class UsuarioController(private val usuarioRepository: UsuarioRepository) {

    @GetMapping
    fun listar(): List<Usuario> = usuarioRepository.findAll()

    @GetMapping("/academia/{academiaId}")
    fun porAcademia(@PathVariable academiaId: Long): List<Usuario> =
        usuarioRepository.findAllByAcademiaId(academiaId)

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): ResponseEntity<Usuario> =
        usuarioRepository.findById(id).map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())

    @PostMapping
    fun criar(@RequestBody usuario: Usuario): Usuario = usuarioRepository.save(usuario)

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @RequestBody atualizada: Usuario): ResponseEntity<Usuario> {
        return usuarioRepository.findById(id).map {
            val novo = it.copy(
                nome = atualizada.nome,
                email = atualizada.email,
                nivelExperiencia = atualizada.nivelExperiencia,
                academia = atualizada.academia
            )
            ResponseEntity.ok(usuarioRepository.save(novo))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long): ResponseEntity<Void> {
        return usuarioRepository.findById(id).map {
            usuarioRepository.delete(it)
            ResponseEntity.noContent().build()
        }.orElse(ResponseEntity.notFound().build())
    }
}
