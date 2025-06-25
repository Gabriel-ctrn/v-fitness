package br.com.vfitness.demo.controller

import br.com.vfitness.demo.entity.Assistente
import br.com.vfitness.demo.service.AssistenteService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import br.com.vfitness.demo.controller.NovoAssistenteRequest
import br.com.vfitness.demo.entity.Academia

@RestController
@RequestMapping("/assistentes")
class AssistenteController(
    private val assistenteService: AssistenteService
) {

    @GetMapping
    fun listar(): List<Assistente> = assistenteService.listar()

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): ResponseEntity<Assistente> =
        assistenteService.buscarPorId(id)

    @PostMapping
    fun criarAssistente(@RequestBody request: NovoAssistenteRequest): ResponseEntity<Academia> {
        val academia = assistenteService.criarAssistente(request)
        return ResponseEntity.ok(academia)
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @RequestBody atualizada: AtualizaAssistenteRequest): ResponseEntity<Assistente> =
        assistenteService.atualizar(id, atualizada)

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long): ResponseEntity<Void> =
        assistenteService.deletar(id)
}
