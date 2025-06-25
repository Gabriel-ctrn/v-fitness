package br.com.vfitness.demo.service

import br.com.vfitness.demo.entity.Treino
import br.com.vfitness.demo.repository.TreinoRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class TreinoService(private val treinoRepository: TreinoRepository) {

    fun listar(): List<Treino> = treinoRepository.findAll()

    fun porUsuario(usuarioId: Long): List<Treino> =
        treinoRepository.findAllByUsuarioId(usuarioId)

    fun buscarPorId(id: Long): ResponseEntity<Treino> {
        val treino = treinoRepository.findById(id).orElse(null)
        return treino?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    fun criar(treino: Treino): Treino = treinoRepository.save(treino)

    fun atualizar(id: Long, atualizada: Treino): ResponseEntity<Treino> {
        val existente = treinoRepository.findById(id).orElse(null)
        return if (existente != null) {
            val novo = existente.copy(
                nome = atualizada.nome,
                usuario = atualizada.usuario
            )
            ResponseEntity.ok(treinoRepository.save(novo))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    fun deletar(id: Long): ResponseEntity<Void> {
        val existente = treinoRepository.findById(id).orElse(null)
        return if (existente != null) {
            treinoRepository.delete(existente)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
