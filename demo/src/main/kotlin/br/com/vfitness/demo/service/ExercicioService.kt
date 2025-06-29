package br.com.vfitness.demo.service

import br.com.vfitness.demo.dto.NovoExercicioRequest
import br.com.vfitness.demo.entity.Exercicio
import br.com.vfitness.demo.repository.ExercicioRepository
import br.com.vfitness.demo.repository.TreinoRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ExercicioService(
    private val exercicioRepository: ExercicioRepository,
    private val treinoRepository: TreinoRepository
) {
    fun listar(): List<Exercicio> = exercicioRepository.findAll()

    fun buscarPorId(id: Long): ResponseEntity<Exercicio> {
        val exercicio = exercicioRepository.findById(id).orElse(null)
        return exercicio?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    fun criarComDto(request: NovoExercicioRequest): Exercicio {
        val treino = treinoRepository.findById(request.treinoId).orElseThrow { RuntimeException("Treino não encontrado") }
        return exercicioRepository.save(
            Exercicio(
                nome = request.nome,
                grupoMuscular = request.grupoMuscular,
                tipo = request.tipo,
                cargas = request.cargas,
                series = request.series,
                repeticoes = request.repeticoes,
                treino = treino
            )
        )
    }

    fun atualizar(id: Long, atualizada: Exercicio): ResponseEntity<Exercicio> {
        val existente = exercicioRepository.findById(id).orElse(null)
        return if (existente != null) {
            val novo = existente.copy(
                nome = atualizada.nome,
                grupoMuscular = atualizada.grupoMuscular,
                tipo = atualizada.tipo,
                cargas = atualizada.cargas,
                series = atualizada.series,
                repeticoes = atualizada.repeticoes
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

    fun buscarPorTreinoId(treinoId: Long): List<Exercicio> =
        exercicioRepository.findAllByTreinoId(treinoId)
}
