package br.com.vfitness.demo.service

import br.com.vfitness.demo.controller.AtualizaAssistenteRequest
import br.com.vfitness.demo.controller.NovoAssistenteRequest
import br.com.vfitness.demo.entity.Academia
import br.com.vfitness.demo.entity.Assistente
import br.com.vfitness.demo.repository.AcademiaRepository
import br.com.vfitness.demo.repository.AssistenteRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AssistenteService(
    private val assistenteRepository: AssistenteRepository,
    private val academiaRepository: AcademiaRepository
) {

    fun listar(): List<Assistente> = assistenteRepository.findAll()

    fun buscarPorId(id: Long): ResponseEntity<Assistente> {
        val assistente = assistenteRepository.findById(id).orElse(null)
        return assistente?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    fun criarAssistente(request: NovoAssistenteRequest): Academia {
        val academia = academiaRepository.findById(request.academiaId)
            .orElseThrow { RuntimeException("Academia não encontrada") }

        val assistente = Assistente(
            nome = request.nome,
            especialidade = request.especialidade,
            academia = academia
        )

        assistenteRepository.save(assistente)
        return academia
    }

    fun atualizar(id: Long, atualizada: AtualizaAssistenteRequest): ResponseEntity<Assistente> {
        val existente = assistenteRepository.findById(id).orElse(null)
        return if (existente != null) {
            val academia = academiaRepository.findById(atualizada.academiaId)
                .orElseThrow { RuntimeException("Academia não encontrada") }
            val novo = existente.copy(
                nome = atualizada.nome,
                especialidade = atualizada.especialidade,
                academia = academia
            )
            ResponseEntity.ok(assistenteRepository.save(novo))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    fun deletar(id: Long): ResponseEntity<Void> {
        val existente = assistenteRepository.findById(id).orElse(null)
        return if (existente != null) {
            assistenteRepository.delete(existente)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
