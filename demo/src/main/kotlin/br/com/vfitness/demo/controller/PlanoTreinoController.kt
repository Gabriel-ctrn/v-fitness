package br.com.vfitness.demo.controller

import br.com.vfitness.demo.dto.NovoPlanoTreinoRequest
import br.com.vfitness.demo.entity.PlanoTreino
import br.com.vfitness.demo.service.PlanoTreinoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/planos-treino")
class PlanoTreinoController(private val planoTreinoService: PlanoTreinoService) {

    @GetMapping
    fun listar(): List<PlanoTreino> = planoTreinoService.listar()

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): ResponseEntity<PlanoTreino> =
        planoTreinoService.buscarPorId(id)

    @PostMapping
    fun criar(@RequestBody request: NovoPlanoTreinoRequest): PlanoTreino =
        planoTreinoService.criar(request)

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long): ResponseEntity<Void> =
        planoTreinoService.deletar(id)
}
