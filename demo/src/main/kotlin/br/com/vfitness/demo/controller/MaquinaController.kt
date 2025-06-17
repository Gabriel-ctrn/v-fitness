package br.com.vfitness.demo.controller

import br.com.vfitness.demo.entity.Maquina
import br.com.vfitness.demo.repository.MaquinaRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/maquinas")
class MaquinaController(private val maquinaRepository: MaquinaRepository) {

    @GetMapping
    fun listar(): List<Maquina> = maquinaRepository.findAll()

    @GetMapping("/academia/{academiaId}")
    fun porAcademia(@PathVariable academiaId: Long): List<Maquina> =
        maquinaRepository.findAllByAcademiaId(academiaId)

    @GetMapping("/grupo/{grupo}")
    fun porGrupoMuscular(@PathVariable grupo: String): List<Maquina> =
        maquinaRepository.findAllByGrupoMuscular(grupo)

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): ResponseEntity<Maquina> =
        maquinaRepository.findById(id).map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())

    @PostMapping
    fun criar(@RequestBody maquina: Maquina): Maquina = maquinaRepository.save(maquina)

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @RequestBody atualizada: Maquina): ResponseEntity<Maquina> {
        return maquinaRepository.findById(id).map {
            val nova = it.copy(nome = atualizada.nome, grupoMuscular = atualizada.grupoMuscular, academia = atualizada.academia)
            ResponseEntity.ok(maquinaRepository.save(nova))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long): ResponseEntity<Void> {
        val maquinaOptional = maquinaRepository.findById(id)

        return if (maquinaOptional.isPresent) {
            maquinaRepository.delete(maquinaOptional.get())
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}