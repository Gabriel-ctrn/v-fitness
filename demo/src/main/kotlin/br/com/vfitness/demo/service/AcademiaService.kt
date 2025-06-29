package br.com.vfitness.demo.service

import br.com.vfitness.demo.entity.Academia
import br.com.vfitness.demo.repository.AcademiaRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AcademiaService(private val academiaRepository: AcademiaRepository) {

    fun listar(): List<Academia> = academiaRepository.findAll()

    fun buscarPorId(id: Long): ResponseEntity<Academia> {
        val academia = academiaRepository.findById(id).orElse(null)
        return academia?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    fun criar(academia: Academia): Academia = academiaRepository.save(academia)

    fun atualizar(id: Long, atualizada: Academia): ResponseEntity<Academia> {
        val existente = academiaRepository.findById(id).orElse(null)
        return if (existente != null) {
            val nova = existente.copy(nome = atualizada.nome, endereco = atualizada.endereco)
            ResponseEntity.ok(academiaRepository.save(nova))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    fun deletar(id: Long): ResponseEntity<Void> {
        val existente = academiaRepository.findById(id).orElse(null)
        return if (existente != null) {
            academiaRepository.delete(existente)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
