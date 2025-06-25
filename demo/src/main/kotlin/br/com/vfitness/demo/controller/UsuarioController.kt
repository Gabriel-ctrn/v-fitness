package br.com.vfitness.demo.controller

import br.com.vfitness.demo.entity.Usuario
import br.com.vfitness.demo.service.UsuarioService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuarios")
class UsuarioController(private val usuarioService: UsuarioService) {

    @GetMapping
    fun listar(): List<Usuario> = usuarioService.listar()

    @GetMapping("/academia/{academiaId}")
    fun porAcademia(@PathVariable academiaId: Long): List<Usuario> =
        usuarioService.porAcademia(academiaId)

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): ResponseEntity<Usuario> =
        usuarioService.buscarPorId(id)

    @PostMapping
    fun criar(@RequestBody usuario: Usuario): Usuario =
        usuarioService.criar(usuario)

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @RequestBody atualizada: Usuario): ResponseEntity<Usuario> =
        usuarioService.atualizar(id, atualizada)

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long): ResponseEntity<Void> =
        usuarioService.deletar(id)
}
