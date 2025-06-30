package br.com.vfitness.demo.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class IAClient(
    @Value("\${gemini.api.key}") private val apiKey: String,
    @Value("\${gemini.api.model}") private val model: String
) {
    private val webClient = WebClient.builder()
        .baseUrl("https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent")
        .defaultHeader("Content-Type", "application/json")
        .build()

    fun obterRespostaAssistente(pergunta: String): String {
        val requestBody = mapOf(
            "contents" to listOf(mapOf("parts" to listOf(mapOf("text" to pergunta))))
        )
        val response = webClient.post()
            .uri("?key=$apiKey")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(String::class.java)
            .block()
        return response ?: "Erro ao obter resposta da IA"
    }
}
