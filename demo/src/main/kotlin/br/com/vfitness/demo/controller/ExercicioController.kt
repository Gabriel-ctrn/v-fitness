package br.com.vfitness.demo.controller

import br.com.vfitness.demo.entity.Exercicio
import br.com.vfitness.demo.service.ExercicioService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/exercicios")
class ExercicioController(private val exercicioService: ExercicioService) {

    @GetMapping
    fun listar(): List<Exercicio> = exercicioService.listar()

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): ResponseEntity<Exercicio> =
        exercicioService.buscarPorId(id)

    @PostMapping
    fun criar(@RequestBody exercicio: Exercicio): Exercicio =
        exercicioService.criar(exercicio)

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @RequestBody atualizada: Exercicio): ResponseEntity<Exercicio> =
        exercicioService.atualizar(id, atualizada)

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long): ResponseEntity<Void> =
        exercicioService.deletar(id)
}
