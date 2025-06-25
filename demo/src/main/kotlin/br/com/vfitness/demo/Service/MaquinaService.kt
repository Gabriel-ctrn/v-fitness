package br.com.vfitness.demo.service

import br.com.vfitness.demo.entity.Maquina
import br.com.vfitness.demo.repository.MaquinaRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class MaquinaService(private val maquinaRepository: MaquinaRepository) {

    fun listar(): List<Maquina> = maquinaRepository.findAll()

    fun porAcademia(academiaId: Long): List<Maquina> =
        maquinaRepository.findAllByAcademiaId(academiaId)

    fun porGrupoMuscular(grupo: String): List<Maquina> =
        maquinaRepository.findAllByGrupoMuscular(grupo)

    fun buscarPorId(id: Long): ResponseEntity<Maquina> {
        val maquina = maquinaRepository.findById(id).orElse(null)
        return maquina?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    fun criar(maquina: Maquina): Maquina = maquinaRepository.save(maquina)

    fun atualizar(id: Long, atualizada: Maquina): ResponseEntity<Maquina> {
        val existente = maquinaRepository.findById(id).orElse(null)
        return if (existente != null) {
            val nova = existente.copy(
                nome = atualizada.nome,
                grupoMuscular = atualizada.grupoMuscular,
                academia = atualizada.academia
            )
            ResponseEntity.ok(maquinaRepository.save(nova))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    fun deletar(id: Long): ResponseEntity<Void> {
        val existente = maquinaRepository.findById(id).orElse(null)
        return if (existente != null) {
            maquinaRepository.delete(existente)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
