package br.com.vfitness.demo.controller

import br.com.vfitness.demo.entity.Assistente
import br.com.vfitness.demo.service.AssistenteService
import br.com.vfitness.demo.dto.NovoAssistenteRequest
import br.com.vfitness.demo.entity.Academia
import br.com.vfitness.demo.dto.AtualizaAssistenteRequest
import br.com.vfitness.demo.dto.SugestaoTreinoDTO
import br.com.vfitness.demo.dto.SugestaoExercicioRequest
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.http.ResponseEntity

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

    @PostMapping("/perguntar")
    fun perguntarAssistente(@RequestBody pergunta: String): ResponseEntity<String> {
        val resposta = assistenteService.obterRespostaAssistente(pergunta)
        return ResponseEntity.ok(resposta)
    }

    @PostMapping("/sugestao-treino")
    fun sugestaoTreino(@RequestBody pergunta: String): ResponseEntity<SugestaoTreinoDTO> {
        val sugestao = assistenteService.obterSugestaoTreino(pergunta)
        return ResponseEntity.ok(sugestao)
    }

    @PostMapping("/sugestao-exercicio")
    fun sugestaoExercicio(
        @RequestBody request: SugestaoExercicioRequest
    ): ResponseEntity<SugestaoTreinoDTO> {
        val sugestao = assistenteService.obterSugestaoTreinoParaTreinoAtual(
            request.treinoId,
            request.exercicioId
        )
        return ResponseEntity.ok(sugestao)
    }
}
