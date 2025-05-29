package br.com.vfitness.demo.controller

import br.com.vfitness.demo.entity.Treino
import br.com.vfitness.demo.repository.TreinoRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/treinos")
class TreinoController(private val treinoRepository: TreinoRepository) {

    @GetMapping
    fun listar(): List<Treino> = treinoRepository.findAll()

    @GetMapping("/usuario/{usuarioId}")
    fun porUsuario(@PathVariable usuarioId: Long): List<Treino> =
        treinoRepository.findAllByUsuarioId(usuarioId)

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): ResponseEntity<Treino> =
        treinoRepository.findById(id).map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())

    @PostMapping
    fun criar(@RequestBody treino: Treino): Treino = treinoRepository.save(treino)

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @RequestBody atualizada: Treino): ResponseEntity<Treino> {
        return treinoRepository.findById(id).map {
            val novo = it.copy(nome = atualizada.nome, usuario = atualizada.usuario)
            ResponseEntity.ok(treinoRepository.save(novo))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long): ResponseEntity<Void> {
        return treinoRepository.findById(id).map {
            treinoRepository.delete(it)
            ResponseEntity.noContent().build()
        }.orElse(ResponseEntity.notFound().build())
    }
}
