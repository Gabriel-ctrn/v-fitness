package br.com.vfitness.demo.service

import br.com.vfitness.demo.dto.SalvarTreinoRequest
import br.com.vfitness.demo.entity.ItemTreino
import br.com.vfitness.demo.entity.Treino
import br.com.vfitness.demo.repository.ExercicioRepository
import br.com.vfitness.demo.repository.ItemTreinoRepository
import br.com.vfitness.demo.repository.TreinoRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TreinoService(
    private val treinoRepository: TreinoRepository,
    private val itemTreinoRepository: ItemTreinoRepository,
    private val exercicioRepository: ExercicioRepository
) {

    fun listar(): List<Treino> = treinoRepository.findAll()

    fun porUsuario(usuarioId: Long): List<Treino> = treinoRepository.findAllByUsuarioId(usuarioId)

    fun buscarPorId(id: Long): ResponseEntity<Treino> {
        return treinoRepository.findById(id)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())
    }

    fun criar(treino: Treino): Treino = treinoRepository.save(treino)

    fun atualizar(id: Long, treinoAtualizado: Treino): ResponseEntity<Treino> {
        return treinoRepository.findById(id).map { treinoExistente ->
            // Aqui você definiria quais campos podem ser atualizados
            val novoTreino = treinoExistente.copy(nome = treinoAtualizado.nome)
            ResponseEntity.ok(treinoRepository.save(novoTreino))
        }.orElse(ResponseEntity.notFound().build())
    }

    fun deletar(id: Long): ResponseEntity<Void> {
        return treinoRepository.findById(id).map { treino ->
            treinoRepository.delete(treino)
            ResponseEntity.noContent().build<Void>()
        }.orElse(ResponseEntity.notFound().build())
    }

    fun finalizarESalvar(request: SalvarTreinoRequest): Treino {
        val treino = treinoRepository.findById(request.treinoId)
            .orElseThrow { RuntimeException("Treino não encontrado com ID: ${request.treinoId}") }

        treino.fim = LocalDateTime.now()

        request.itens.forEach { itemDTO ->
            val exercicio = exercicioRepository.findById(itemDTO.exercicioId)
                .orElseThrow { RuntimeException("Exercício não encontrado com ID: ${itemDTO.exercicioId}") }

            itemDTO.series.forEach { serieDTO ->
                val novoItemTreino = ItemTreino(
                    exercicio = exercicio.nome,
                    carga = serieDTO.carga,
                    repeticoes = serieDTO.repeticoes.toString(),
                    treino = treino
                )
                itemTreinoRepository.save(novoItemTreino)
            }
        }

        return treinoRepository.save(treino)
    }
}