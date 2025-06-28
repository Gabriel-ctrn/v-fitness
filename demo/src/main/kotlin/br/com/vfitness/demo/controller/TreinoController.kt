package br.com.vfitness.demo.controller

import br.com.vfitness.demo.dto.SalvarTreinoRequest
import br.com.vfitness.demo.entity.Treino
import br.com.vfitness.demo.service.TreinoService
import org.springframework.http.HttpStatus
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
    fun criar(@RequestBody treino: Treino, @RequestHeader("X-Perfil") perfil: String): ResponseEntity<Any> {
        return if (perfil == "ALUNO") ResponseEntity.ok(treinoService.criar(treino))
        else ResponseEntity.status(HttpStatus.FORBIDDEN).body("Apenas ALUNO pode criar treinos.")
    }

    @PutMapping("/finalizar")
    fun finalizar(@RequestBody request: SalvarTreinoRequest, @RequestHeader("X-Perfil") perfil: String): ResponseEntity<Any> {
        return if (perfil == "ALUNO") {
            try {
                val treinoSalvo = treinoService.finalizarESalvar(request)
                ResponseEntity.ok(treinoSalvo)
            } catch (e: RuntimeException) {
                ResponseEntity.notFound().build()
            }
        } else {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("Apenas ALUNO pode finalizar treinos.")
        }
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long): ResponseEntity<Void> =
        treinoService.deletar(id)

    @GetMapping("/{id}/recomendacao")
    fun recomendar(@PathVariable id: Long): ResponseEntity<String> {
        val treino = treinoService.buscarPorId(id).body ?: return ResponseEntity.notFound().build()
        // Lógica simples de recomendação: se todas as repetições forem >= 10, sugerir aumento de carga
        val sugestoes = treino.itens.mapNotNull {
            val rep = it.repeticoes.filter { c -> c.isDigit() }.toIntOrNull() ?: 0
            if (rep >= 10) "Considere aumentar a carga do exercício ${it.exercicio}." else null
        }
        val msg = if (sugestoes.isNotEmpty()) sugestoes.joinToString(" ") else "Mantenha o treino atual."
        return ResponseEntity.ok(msg)
    }
}