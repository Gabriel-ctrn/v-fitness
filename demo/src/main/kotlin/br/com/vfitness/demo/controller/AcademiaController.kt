package br.com.vfitness.demo.controller

import br.com.vfitness.demo.entity.Academia
import br.com.vfitness.demo.repository.AcademiaRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/academias")
class AcademiaController(private val academiaRepository: AcademiaRepository) {

    @GetMapping
    fun listar(): List<Academia> = academiaRepository.findAll()

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): ResponseEntity<Academia> =
        academiaRepository.findById(id).map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())

    @PostMapping
    fun criar(@RequestBody academia: Academia): Academia = academiaRepository.save(academia)

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @RequestBody atualizada: Academia): ResponseEntity<Academia> {
        return academiaRepository.findById(id).map {
            val nova = it.copy(nome = atualizada.nome, endereco = atualizada.endereco)
            ResponseEntity.ok(academiaRepository.save(nova))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long): ResponseEntity<Void> {
        return academiaRepository.findById(id).map {
            academiaRepository.delete(it)
            ResponseEntity.noContent().build()
        }.orElse(ResponseEntity.notFound().build())
    }
}
