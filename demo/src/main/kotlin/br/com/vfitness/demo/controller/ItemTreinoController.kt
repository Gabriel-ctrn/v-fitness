package br.com.vfitness.demo.controller

import br.com.vfitness.demo.entity.ItemTreino
import br.com.vfitness.demo.repository.ItemTreinoRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/itens-treino")
class ItemTreinoController(private val itemTreinoRepository: ItemTreinoRepository) {

    @GetMapping
    fun listar(): List<ItemTreino> = itemTreinoRepository.findAll()

    @GetMapping("/treino/{treinoId}")
    fun porTreino(@PathVariable treinoId: Long): List<ItemTreino> =
        itemTreinoRepository.findAllByTreinoId(treinoId)

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): ResponseEntity<ItemTreino> =
        itemTreinoRepository.findById(id).map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())

    @PostMapping
    fun criar(@RequestBody itemTreino: ItemTreino): ItemTreino = itemTreinoRepository.save(itemTreino)

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @RequestBody atualizada: ItemTreino): ResponseEntity<ItemTreino> {
        return itemTreinoRepository.findById(id).map {
            val novo = it.copy(
                exercicio = atualizada.exercicio,
                carga = atualizada.carga,
                repeticoes = atualizada.repeticoes,
                treino = atualizada.treino
            )
            ResponseEntity.ok(itemTreinoRepository.save(novo))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long): ResponseEntity<Void> {
        return itemTreinoRepository.findById(id).map {
            itemTreinoRepository.delete(it)
            ResponseEntity.noContent().build()
        }.orElse(ResponseEntity.notFound().build())
    }
}
