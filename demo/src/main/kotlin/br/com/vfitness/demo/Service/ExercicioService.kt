package br.com.vfitness.demo.service

import br.com.vfitness.demo.entity.Exercicio
import br.com.vfitness.demo.repository.ExercicioRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ExercicioService(private val exercicioRepository: ExercicioRepository) {

    fun listar(): List<Exercicio> = exercicioRepository.findAll()

    fun buscarPorId(id: Long): ResponseEntity<Exercicio> {
        val exercicio = exercicioRepository.findById(id).orElse(null)
        return exercicio?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    fun criar(exercicio: Exercicio): Exercicio = exercicioRepository.save(exercicio)

    fun atualizar(id: Long, atualizada: Exercicio): ResponseEntity<Exercicio> {
        val existente = exercicioRepository.findById(id).orElse(null)
        return if (existente != null) {
            val novo = existente.copy(
                nome = atualizada.nome,
                grupoMuscular = atualizada.grupoMuscular
            )
            ResponseEntity.ok(exercicioRepository.save(novo))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    fun deletar(id: Long): ResponseEntity<Void> {
        val existente = exercicioRepository.findById(id).orElse(null)
        return if (existente != null) {
            exercicioRepository.delete(existente)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}