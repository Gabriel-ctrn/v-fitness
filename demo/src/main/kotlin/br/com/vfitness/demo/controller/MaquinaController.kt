package br.com.vfitness.demo.controller

import br.com.vfitness.demo.entity.Maquina
import br.com.vfitness.demo.service.MaquinaService
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
    fun buscarPorId(@PathVariable id: Long): ResponseEntity<Maquina> =
        maquinaService.buscarPorId(id)

    @PostMapping
    fun criar(@RequestBody maquina: Maquina): Maquina = maquinaService.criar(maquina)

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @RequestBody atualizada: Maquina): ResponseEntity<Maquina> =
        maquinaService.atualizar(id, atualizada)

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long): ResponseEntity<Void> =
        maquinaService.deletar(id)
}
