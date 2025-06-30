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
import br.com.vfitness.demo.repository.ExercicioRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AssistenteService(
    private val assistenteRepository: AssistenteRepository,
    private val academiaRepository: AcademiaRepository,
    private val iaClient: IAClient, // injeta o client da IA
    private val treinoRepository: TreinoRepository,
    private val maquinaRepository: MaquinaRepository,
    private val itemTreinoRepository: ItemTreinoRepository,
    private val exercicioRepository: ExercicioRepository
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

    fun obterSugestaoTreinoParaTreinoAtual(treinoId: Long, exercicioId: Long): SugestaoTreinoDTO {
        // Busca todos os exercícios do treino
        val exercicios = exercicioRepository.findAllByTreinoId(treinoId)
        // Busca o exercício específico
        val exercicioOcupado = exercicios.find { it.id == exercicioId }
        // Monta o prompt para a IA
        val prompt = """
Você é um especialista em treinos de musculação e precisa sugerir dois novos exercícios com base no contexto:
Exercício indisponível: ${exercicioOcupado?.nome ?: ""} (grupo muscular: ${exercicioOcupado?.grupoMuscular ?: ""})

Recomende de forma resumida:
1. Um exercício em outra máquina que tenha o mesmo propósito do treino atual.
2. Um exercício com pesos livres, também com o mesmo propósito.

Atenção: Sempre informe a carga sugerida em kg (quilogramas), nunca em porcentagem de 1RM ou outros valores relativos.

Responda em JSON compatível com o seguinte modelo:
{
  "maquina": {
    "nome": "...",
    "carga": "...",
    "series": "...",
    "repeticoes": "..."
  },
  "livre": {
    "nome": "...",
    "carga": "...",
    "series": "...",
    "repeticoes": "..."
  }
}
"""
        return try {
            val respostaJson = iaClient.obterRespostaAssistente(prompt)
            val mapper = com.fasterxml.jackson.module.kotlin.jacksonObjectMapper()
            mapper.readValue(respostaJson, SugestaoTreinoDTO::class.java)
        } catch (e: Exception) {
            // Retorna vazio ou lança exceção conforme sua estratégia
            SugestaoTreinoDTO(emptyList(), emptyMap())
        }
    }

    fun montarPromptParaIA(treino: Treino, itens: List<ItemTreino>, maquinasOcupadas: List<String>): String {
        val exercicios = itens.joinToString("; ") { "${it.exercicio} (carga: ${it.carga}, reps: ${it.repeticoes})" }
        val maquinas = maquinasOcupadas.joinToString(", ")
        return "Sugira alternativas para o treino '${treino.nome}' considerando que as máquinas ocupadas são: $maquinas. Exercícios planejados: $exercicios. Sugira substituições livres ou em outras máquinas, se necessário, e aumentos de carga se apropriado. Responda em JSON compatível com SugestaoTreinoDTO."
    }
}
