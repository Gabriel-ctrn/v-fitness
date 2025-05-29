package br.com.vfitness.demo.controller

import br.com.vfitness.demo.entity.Exercicio
import br.com.vfitness.demo.repository.ExercicioRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/exercicios")
class ExercicioController(private val exercicioRepository: ExercicioRepository) {

    @GetMapping
    fun listar(): List<Exercicio> = exercicioRepository.findAll()

    @GetMapping("/grupo/{grupo}")
    fun porGrupo(@PathVariable grupo: String): List<Exercicio> =
        exercicioRepository.findAllByGrupoMuscular(grupo)

    @GetMapping("/tipo/{tipo}")
    fun porTipo(@PathVariable tipo: String): List<Exercicio> =
        exercicioRepository.findAllByTipo(tipo)

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): ResponseEntity<Exercicio> =
        exercicioRepository.findById(id).map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())

    @PostMapping
    fun criar(@RequestBody exercicio: Exercicio): Exercicio = exercicioRepository.save(exercicio)

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @RequestBody atualizada: Exercicio): ResponseEntity<Exercicio> {
        return exercicioRepository.findById(id).map {
            val novo = it.copy(
                nome = atualizada.nome,
                grupoMuscular = atualizada.grupoMuscular,
                tipo = atualizada.tipo
            )
            ResponseEntity.ok(exercicioRepository.save(novo))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long): ResponseEntity<Void> {
        return exercicioRepository.findById(id).map {
            exercicioRepository.delete(it)
            ResponseEntity.noContent().build()
        }.orElse(ResponseEntity.notFound().build())
    }
}
