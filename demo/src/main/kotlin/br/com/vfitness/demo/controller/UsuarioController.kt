package br.com.vfitness.demo.controller

import br.com.vfitness.demo.entity.Usuario
import br.com.vfitness.demo.service.UsuarioService
import br.com.vfitness.demo.dto.NovoUsuarioRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin

data class LoginRequest(val email: String, val senha: String)

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = ["http://localhost:8081"])
class UsuarioController(private val usuarioService: UsuarioService) {

    @GetMapping
    fun listar(): List<Usuario> = usuarioService.listar()

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): ResponseEntity<Usuario> =
        usuarioService.buscarPorId(id)

    @PostMapping
    fun criar(@RequestBody request: NovoUsuarioRequest): Usuario =
        usuarioService.criarComDto(request)

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @RequestBody atualizada: Usuario): ResponseEntity<Usuario> =
        usuarioService.atualizar(id, atualizada)

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long): ResponseEntity<Void> =
        usuarioService.deletar(id)

    @PostMapping("/login")
    fun login(@RequestBody login: LoginRequest): ResponseEntity<Usuario> {
        val usuario = usuarioService.autenticar(login.email, login.senha)
        return if (usuario != null) ResponseEntity.ok(usuario)
        else ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }
}