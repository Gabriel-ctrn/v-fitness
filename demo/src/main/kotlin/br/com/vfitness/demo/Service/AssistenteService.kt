package br.com.vfitness.demo.service

import br.com.vfitness.demo.client.IAClient
import br.com.vfitness.demo.dto.AtualizaAssistenteRequest
// Ensure this import path matches the actual location of NovoAssistenteRequest
import br.com.vfitness.demo.dto.NovoAssistenteRequest
import br.com.vfitness.demo.dto.SugestaoTreinoDTO
import br.com.vfitness.demo.entity.Academia
import br.com.vfitness.demo.entity.Assistente
import br.com.vfitness.demo.entity.Treino
import br.com.vfitness.demo.entity.ItemTreino
import br.com.vfitness.demo.repository.AcademiaRepository
import br.com.vfitness.demo.repository.AssistenteRepository
import br.com.vfitness.demo.repository.TreinoRepository
import br.com.vfitness.demo.repository.MaquinaRepository
import br.com.vfitness.demo.repository.ItemTreinoRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AssistenteService(
    private val assistenteRepository: AssistenteRepository,
    private val academiaRepository: AcademiaRepository,
    private val iaClient: IAClient, // injeta o client da IA
    private val treinoRepository: TreinoRepository,
    private val maquinaRepository: MaquinaRepository,
    private val itemTreinoRepository: ItemTreinoRepository
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
            identificador = request.identificador,
            modelo = request.modelo,
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
                identificador = atualizada.identificador,
                modelo = atualizada.modelo,
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

    fun obterRespostaAssistente(pergunta: String): String {
        // Chama o client da IA para obter a resposta
        return iaClient.obterRespostaAssistente(pergunta)
    }

    fun obterSugestaoTreino(pergunta: String): SugestaoTreinoDTO {
        // Chama a IA e espera resposta em formato JSON
        val respostaJson = iaClient.obterRespostaAssistente(pergunta)
        // Aqui você pode usar uma lib como Jackson para converter o JSON em DTO
        return try {
            val mapper = com.fasterxml.jackson.module.kotlin.jacksonObjectMapper()
            mapper.readValue(respostaJson, SugestaoTreinoDTO::class.java)
        } catch (e: Exception) {
            // Retorna vazio ou lança exceção conforme sua estratégia
            SugestaoTreinoDTO(emptyList(), emptyMap())
        }
    }

    fun obterSugestaoTreinoParaTreinoAtual(treinoId: Long, maquinasOcupadas: List<String>): SugestaoTreinoDTO {
        // Busca o treino atual
        val treino = treinoRepository.findById(treinoId).orElseThrow { RuntimeException("Treino não encontrado") }
        // Busca os itens do treino
        val itens = itemTreinoRepository.findAllByTreinoId(treinoId)
        // Monta o prompt para a IA
        val prompt = montarPromptParaIA(treino, itens, maquinasOcupadas)
        val respostaJson = iaClient.obterRespostaAssistente(prompt)
        return try {
            val mapper = com.fasterxml.jackson.module.kotlin.jacksonObjectMapper()
            mapper.readValue(respostaJson, SugestaoTreinoDTO::class.java)
        } catch (e: Exception) {
            SugestaoTreinoDTO(emptyList(), emptyMap())
        }
    }

    fun montarPromptParaIA(treino: Treino, itens: List<ItemTreino>, maquinasOcupadas: List<String>): String {
        val exercicios = itens.joinToString("; ") { "${it.exercicio} (carga: ${it.carga}, reps: ${it.repeticoes})" }
        val maquinas = maquinasOcupadas.joinToString(", ")
        return "Sugira alternativas para o treino '${treino.nome}' considerando que as máquinas ocupadas são: $maquinas. Exercícios planejados: $exercicios. Sugira substituições livres ou em outras máquinas, se necessário, e aumentos de carga se apropriado. Responda em JSON compatível com SugestaoTreinoDTO."
    }
}
