package br.com.vfitness.demo.service

import br.com.vfitness.demo.controller.NovoItemTreinoRequest
import br.com.vfitness.demo.entity.ItemTreino
import br.com.vfitness.demo.entity.Treino
import br.com.vfitness.demo.repository.ItemTreinoRepository
import br.com.vfitness.demo.repository.TreinoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class ItemTreinoServiceTest {
    private lateinit var itemTreinoRepository: ItemTreinoRepository
    private lateinit var treinoRepository: TreinoRepository
    private lateinit var itemTreinoService: ItemTreinoService

    @BeforeEach
    fun setUp() {
        itemTreinoRepository = mockk(relaxed = true)
        treinoRepository = mockk(relaxed = true)
        itemTreinoService = ItemTreinoService(itemTreinoRepository, treinoRepository)
    }

    @Test
    fun `deve criar um novo item de treino`() {
        val treino = Treino(id = 1L, nome = "Treino A", usuario = mockk(), academia = mockk(), itens = mutableListOf())
        val request = NovoItemTreinoRequest(
            exercicio = "Supino",
            carga = 40,
            repeticoes = 10,
            treinoId = 1L
        )
        every { treinoRepository.findById(1L) } returns Optional.of(treino)
        val slot = slot<ItemTreino>()
        every { itemTreinoRepository.save(capture(slot)) } answers { slot.captured }

        val result = itemTreinoService.criar(request)

        assertEquals("Supino", result.exercicio)
        assertEquals(40, result.carga)
        assertEquals(10, result.repeticoes)
        assertEquals(treino, result.treino)
        verify { itemTreinoRepository.save(any()) }
    }
}
