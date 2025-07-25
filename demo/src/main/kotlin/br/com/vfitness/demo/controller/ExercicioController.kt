package br.com.vfitness.demo.controller

import br.com.vfitness.demo.entity.Exercicio
import br.com.vfitness.demo.service.ExercicioService
import br.com.vfitness.demo.dto.NovoExercicioRequest
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
    fun criar(@RequestBody request: NovoExercicioRequest): Exercicio =
        exercicioService.criarComDto(request)

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @RequestBody atualizada: Exercicio): ResponseEntity<Exercicio> =
        exercicioService.atualizar(id, atualizada)

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long): ResponseEntity<Void> =
        exercicioService.deletar(id)

    @GetMapping("/treino/{treinoId}")
    fun buscarPorTreinoId(@PathVariable treinoId: Long): List<Exercicio> =
        exercicioService.buscarPorTreinoId(treinoId)
}
