package br.com.vfitness.demo.controller

import br.com.vfitness.demo.entity.Usuario
import br.com.vfitness.demo.service.UsuarioService
import br.com.vfitness.demo.dto.NovoUsuarioRequest
import org.springframework.http.HttpStatus
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
    fun criar(@RequestBody request: NovoUsuarioRequest, @RequestHeader("X-Perfil") perfil: String): ResponseEntity<Any> =
        if (perfil == "ADMIN") ResponseEntity.ok(usuarioService.criarComPerfil(request))
        else ResponseEntity.status(HttpStatus.FORBIDDEN).body("Apenas ADMIN pode cadastrar usuários.")

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @RequestBody atualizada: Usuario): ResponseEntity<Usuario> =
        usuarioService.atualizar(id, atualizada)

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long): ResponseEntity<Void> =
        usuarioService.deletar(id)
}
