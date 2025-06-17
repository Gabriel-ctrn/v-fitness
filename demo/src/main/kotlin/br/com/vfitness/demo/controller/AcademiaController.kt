package br.com.vfitness.demo.controller

import br.com.vfitness.demo.entity.Academia
import br.com.vfitness.demo.repository.AcademiaRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/academias")
class AcademiaController(private val academiaRepository: AcademiaRepository) {

    @GetMapping
    fun listar(): List<Academia> = academiaRepository.findAll()

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): ResponseEntity<Academia> {
        val academiaOptional: Optional<Academia> = academiaRepository.findById(id)
        return academiaOptional.map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun criar(@RequestBody academia: Academia): Academia = academiaRepository.save(academia)

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @RequestBody atualizada: Academia): ResponseEntity<Academia> {
        return academiaRepository.findById(id).map { academiaExistente ->
            val novaAcademia = academiaExistente.copy(
                nome = atualizada.nome,
                endereco = atualizada.endereco
            )
            ResponseEntity.ok(academiaRepository.save(novaAcademia))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long): ResponseEntity<Void> {
        return academiaRepository.findById(id).map { academia ->
            academiaRepository.delete(academia)
            ResponseEntity.noContent().build<Void>()
        }.orElse(ResponseEntity.notFound().build())
    }
}
