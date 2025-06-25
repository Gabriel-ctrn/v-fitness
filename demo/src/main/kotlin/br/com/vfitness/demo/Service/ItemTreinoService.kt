package br.com.vfitness.demo.service

import br.com.vfitness.demo.controller.AtualizaItemTreinoRequest
import br.com.vfitness.demo.controller.NovoItemTreinoRequest
import br.com.vfitness.demo.entity.ItemTreino
import br.com.vfitness.demo.repository.ItemTreinoRepository
import br.com.vfitness.demo.repository.TreinoRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ItemTreinoService(
    private val itemTreinoRepository: ItemTreinoRepository,
    private val treinoRepository: TreinoRepository
) {

    fun listar(): List<ItemTreino> = itemTreinoRepository.findAll()

    fun porTreino(treinoId: Long): List<ItemTreino> =
        itemTreinoRepository.findAllByTreinoId(treinoId)

    fun buscarPorId(id: Long): ResponseEntity<ItemTreino> {
        val item = itemTreinoRepository.findById(id).orElse(null)
        return item?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    fun criar(request: NovoItemTreinoRequest): ItemTreino {
        val treino = treinoRepository.findById(request.treinoId)
            .orElseThrow { RuntimeException("Treino não encontrado") }
        val itemTreino = ItemTreino(
            exercicio = request.exercicio,
            carga = request.carga,
            repeticoes = request.repeticoes,
            treino = treino
        )
        return itemTreinoRepository.save(itemTreino)
    }

    fun atualizar(id: Long, atualizada: AtualizaItemTreinoRequest): ResponseEntity<ItemTreino> {
        val existente = itemTreinoRepository.findById(id).orElse(null)
        return if (existente != null) {
            val treino = treinoRepository.findById(atualizada.treinoId)
                .orElseThrow { RuntimeException("Treino não encontrado") }
            val novo = existente.copy(
                exercicio = atualizada.exercicio,
                carga = atualizada.carga,
                repeticoes = atualizada.repeticoes,
                treino = treino
            )
            ResponseEntity.ok(itemTreinoRepository.save(novo))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    fun deletar(id: Long): ResponseEntity<Void> {
        val existente = itemTreinoRepository.findById(id).orElse(null)
        return if (existente != null) {
            itemTreinoRepository.delete(existente)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
