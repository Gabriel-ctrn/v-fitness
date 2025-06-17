package br.com.vfitness.demo.controller

import br.com.vfitness.demo.entity.Treino
import br.com.vfitness.demo.repository.TreinoRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Duration
import java.time.LocalDateTime

@RestController
@RequestMapping("/treinos")
class TreinoController(private val treinoRepository: TreinoRepository) {

    private val treinosAtivos: MutableMap<Long, Treino> = mutableMapOf()

    @GetMapping
    fun listar(): List<Treino> = treinoRepository.findAll()

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): ResponseEntity<Treino> =
        treinoRepository.findById(id)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())

    @PostMapping
    fun criar(@RequestBody treino: Treino): Treino = treinoRepository.save(treino)


    @PostMapping("/{id}/iniciar")
    fun iniciarTreino(@PathVariable id: Long): ResponseEntity<Treino> {
        val treino = treinoRepository.findById(id).orElse(null)
            ?: return ResponseEntity.notFound().build()

        treino.inicio = LocalDateTime.now()
        treino.ultimoEvento = treino.inicio
        treinosAtivos[id] = treino

        return ResponseEntity.ok(treino)
    }

    @PostMapping("/{id}/descansar")
    fun registrarDescanso(@PathVariable id: Long): ResponseEntity<String> {
        val treinoAtivo = treinosAtivos[id]
            ?: return ResponseEntity.badRequest().body("Treino com ID $id não foi iniciado ou já foi finalizado.")

        val agora = LocalDateTime.now()
        val ultimoEvento = treinoAtivo.ultimoEvento

        if (ultimoEvento != null) {
            val descanso = Duration.between(ultimoEvento, agora)
            treinoAtivo.descansos.add(descanso)
        }

        treinoAtivo.ultimoEvento = agora

        return ResponseEntity.ok("Descanso registrado. Tempo total de descanso agora: ${treinoAtivo.tempoTotalDescanso}")
    }

    @PostMapping("/{id}/finalizar")
    fun finalizarTreino(@PathVariable id: Long): ResponseEntity<Treino> {
        val treinoAtivo = treinosAtivos[id]
        //
            ?: return ResponseEntity.badRequest().build()

        treinoAtivo.fim = LocalDateTime.now()
        treinosAtivos.remove(id)

        // treinoRepository.save(treinoAtivo) // Futuramente, salvar no banco

        return ResponseEntity.ok(treinoAtivo)
    }
}