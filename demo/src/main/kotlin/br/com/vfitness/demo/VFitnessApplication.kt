package br.com.vfitness.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VFitnessApplication

fun main(args: Array<String>) {
	runApplication<VFitnessApplication>(*args)
}
