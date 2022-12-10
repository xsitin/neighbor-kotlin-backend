package ru.neighbor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NeighborApplication
{}

fun main(args: Array<String>) {
    runApplication<NeighborApplication>(*args)
}
