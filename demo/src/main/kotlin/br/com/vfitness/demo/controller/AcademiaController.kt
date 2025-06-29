package br.com.vfitness.demo.controller

import br.com.vfitness.demo.entity.Academia
import br.com.vfitness.demo.service.AcademiaService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.CrossOrigin

@RestController
@RequestMapping("/academias")
@CrossOrigin(origins = ["http://localhost:8081"])
class AcademiaController(private val academiaService: AcademiaService) {

    @GetMapping
    fun listar(): List<Academia> = academiaService.listar()

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): ResponseEntity<Academia> = academiaService.buscarPorId(id)

    @PostMapping
    fun criar(@RequestBody academia: Academia): Academia = academiaService.criar(academia)

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @RequestBody atualizada: Academia): ResponseEntity<Academia> =
        academiaService.atualizar(id, atualizada)

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long): ResponseEntity<Void> = academiaService.deletar(id)
}

