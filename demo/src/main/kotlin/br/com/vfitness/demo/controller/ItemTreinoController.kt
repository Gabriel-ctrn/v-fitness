package br.com.vfitness.demo.controller

import br.com.vfitness.demo.entity.ItemTreino
import br.com.vfitness.demo.service.ItemTreinoService
import br.com.vfitness.demo.dto.NovoItemTreinoRequest
import br.com.vfitness.demo.dto.AtualizaItemTreinoRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/itens-treino")
class ItemTreinoController(private val itemTreinoService: ItemTreinoService) {

    @GetMapping
    fun listar(): List<ItemTreino> = itemTreinoService.listar()

    @GetMapping("/treino/{treinoId}")
    fun porTreino(@PathVariable treinoId: Long): List<ItemTreino> =
        itemTreinoService.porTreino(treinoId)

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): ResponseEntity<ItemTreino> =
        itemTreinoService.buscarPorId(id)

    @PostMapping
    fun criar(@RequestBody request: NovoItemTreinoRequest): ItemTreino =
        itemTreinoService.criar(request)

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @RequestBody atualizada: AtualizaItemTreinoRequest): ResponseEntity<ItemTreino> =
        itemTreinoService.atualizar(id, atualizada)

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long): ResponseEntity<Void> =
        itemTreinoService.deletar(id)
}
