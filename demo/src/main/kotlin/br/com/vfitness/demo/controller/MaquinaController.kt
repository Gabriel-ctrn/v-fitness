package br.com.vfitness.demo.controller

import br.com.vfitness.demo.entity.Maquina
import br.com.vfitness.demo.service.MaquinaService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/maquinas")
class MaquinaController(private val maquinaService: MaquinaService) {

    @GetMapping
    fun listar(): List<Maquina> = maquinaService.listar()

    @GetMapping("/academia/{academiaId}")
    fun porAcademia(@PathVariable academiaId: Long): List<Maquina> =
        maquinaService.porAcademia(academiaId)

    @GetMapping("/grupo/{grupo}")
    fun porGrupoMuscular(@PathVariable grupo: String): List<Maquina> =
        maquinaService.porGrupoMuscular(grupo)

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): ResponseEntity<Any> {
        val result = maquinaService.buscarPorId(id)
        return ResponseEntity.status(result.statusCode).body(result.body)
    }

    @PostMapping
    fun criar(@RequestBody maquina: Maquina, @RequestHeader("X-Perfil") perfil: String): ResponseEntity<Any> =
        if (perfil == "ADMIN") ResponseEntity.ok(maquinaService.criar(maquina))
        else ResponseEntity.status(HttpStatus.FORBIDDEN).body("Apenas ADMIN pode cadastrar máquinas.")

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @RequestBody atualizada: Maquina, @RequestHeader("X-Perfil") perfil: String): ResponseEntity<Any> {
        if (perfil != "ADMIN") {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Apenas ADMIN pode atualizar máquinas.")
        }
        val result = maquinaService.atualizar(id, atualizada)
        return ResponseEntity.status(result.statusCode).body(result.body)
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long, @RequestHeader("X-Perfil") perfil: String): ResponseEntity<Any> {
        if (perfil != "ADMIN") {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Apenas ADMIN pode deletar máquinas.")
        }
        val result = maquinaService.deletar(id)
        return ResponseEntity.status(result.statusCode).body(result.body)
    }
}
