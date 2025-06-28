package br.com.vfitness.demo.client

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class IAClient {
    private val apiKey = "AIzaSyCi7QVPMPn0XjBN1zOKQQvGGvFvMOkVxj4"
    private val webClient = WebClient.builder()
        .baseUrl("https://url-da-sua-ia.com")
        .defaultHeader("Authorization", "Bearer $apiKey")
        .build()

    fun obterRespostaAssistente(pergunta: String): String {
        val response = webClient.post()
            .uri("/endpoint-da-ia")
            .bodyValue(mapOf("pergunta" to pergunta))
            .retrieve()
            .bodyToMono(String::class.java)
            .block()
        return response ?: "Erro ao obter resposta da IA"
    }
}