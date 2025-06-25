package br.com.vfitness.demo.controller

import br.com.vfitness.demo.dto.SalvarTreinoRequest
import br.com.vfitness.demo.entity.Treino
import br.com.vfitness.demo.service.TreinoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/treinos")
class TreinoController(private val treinoService: TreinoService) {

    @GetMapping
    fun listar(): List<Treino> = treinoService.listar()

    @GetMapping("/usuario/{usuarioId}")
    fun porUsuario(@PathVariable usuarioId: Long): List<Treino> =
        treinoService.porUsuario(usuarioId)

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): ResponseEntity<Treino> =
        treinoService.buscarPorId(id)

    @PostMapping
    fun criar(@RequestBody treino: Treino): Treino =
        treinoService.criar(treino)

    @PutMapping("/finalizar")
    fun finalizar(@RequestBody request: SalvarTreinoRequest): ResponseEntity<Treino> {
        return try {
            val treinoSalvo = treinoService.finalizarESalvar(request)
            ResponseEntity.ok(treinoSalvo)
        } catch (e: RuntimeException) {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long): ResponseEntity<Void> =
        treinoService.deletar(id)
}