package br.com.vfitness.demo.service

import br.com.vfitness.demo.dto.NovoPlanoTreinoRequest
import br.com.vfitness.demo.entity.PlanoTreino
import br.com.vfitness.demo.repository.PlanoTreinoRepository
import br.com.vfitness.demo.repository.UsuarioRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class PlanoTreinoService(
    private val planoTreinoRepository: PlanoTreinoRepository,
    private val usuarioRepository: UsuarioRepository
) {
    fun listar(): List<PlanoTreino> = planoTreinoRepository.findAll()

    fun buscarPorId(id: Long): ResponseEntity<PlanoTreino> {
        val plano = planoTreinoRepository.findById(id).orElse(null)
        return plano?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    fun criar(request: NovoPlanoTreinoRequest): PlanoTreino {
        val usuario = usuarioRepository.findById(request.usuarioId).orElseThrow { RuntimeException("Usuário não encontrado") }
        val plano = PlanoTreino(
            nome = request.nome,
            objetivo = request.objetivo,
            usuario = usuario
        )
        return planoTreinoRepository.save(plano)
    }

    fun deletar(id: Long): ResponseEntity<Void> {
        val existente = planoTreinoRepository.findById(id).orElse(null)
        return if (existente != null) {
            planoTreinoRepository.delete(existente)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
