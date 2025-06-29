package br.com.vfitness.demo.service

import br.com.vfitness.demo.dto.SalvarTreinoRequest
import br.com.vfitness.demo.entity.ItemTreino
import br.com.vfitness.demo.entity.Treino
import br.com.vfitness.demo.repository.ExercicioRepository
import br.com.vfitness.demo.repository.ItemTreinoRepository
import br.com.vfitness.demo.repository.TreinoRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class TreinoService(
    private val treinoRepository: TreinoRepository,
    private val itemTreinoRepository: ItemTreinoRepository,
    private val exercicioRepository: ExercicioRepository
) {
    fun listar(): List<Treino> = treinoRepository.findAll()
    fun porUsuario(usuarioId: Long): List<Treino> = treinoRepository.findAllByUsuarioId(usuarioId)
    fun buscarPorId(id: Long): ResponseEntity<Treino> =
        treinoRepository.findById(id)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())
    fun criar(treino: Treino): Treino = treinoRepository.save(treino)
    fun deletar(id: Long): ResponseEntity<Void> =
        treinoRepository.findById(id).map {
            treinoRepository.delete(it)
            ResponseEntity.noContent().build<Void>()
        }.orElse(ResponseEntity.notFound().build())

    @Transactional
    fun finalizarESalvar(request: SalvarTreinoRequest): Treino {
        val treino = treinoRepository.findById(request.treinoId)
            .orElseThrow { RuntimeException("Treino não encontrado com ID: ${request.treinoId}") }

        // CORREÇÃO: Apaga todos os itens antigos antes de salvar os novos.
        itemTreinoRepository.deleteAllByTreinoId(treino.id)

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
