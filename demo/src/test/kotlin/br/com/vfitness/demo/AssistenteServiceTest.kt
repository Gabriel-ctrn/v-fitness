package br.com.vfitness.demo

import br.com.vfitness.demo.entity.Assistente
import br.com.vfitness.demo.entity.Academia
import br.com.vfitness.demo.repository.AssistenteRepository
import br.com.vfitness.demo.repository.AcademiaRepository
import br.com.vfitness.demo.service.AssistenteService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.whenever
import org.mockito.kotlin.mock
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
class AssistenteServiceTest {

    private val assistenteRepository: AssistenteRepository = mock()
    private val academiaRepository: AcademiaRepository = mock()
    private val assistenteService = AssistenteService(assistenteRepository, academiaRepository)

    @Test
    fun `deve retornar assistente por id`() {
        val academia = Academia(1, "Academia A", "Rua 1")
        val assistente = Assistente(1, "Copilot", "GPT-4", academia)

        whenever(assistenteRepository.findById(1)).thenReturn(Optional.of(assistente))

        val response = assistenteService.buscarPorId(1)

        assertTrue(response.statusCode.is2xxSuccessful)
        assertEquals("Copilot", response.body?.identificador)
        assertEquals("GPT-4", response.body?.modelo)
    }
}
