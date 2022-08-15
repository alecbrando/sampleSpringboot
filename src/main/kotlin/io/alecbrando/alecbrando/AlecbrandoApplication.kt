package io.alecbrando.alecbrando

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BaseApplication

fun main(args: Array<String>) {
	runApplication<BaseApplication>(*args)
}
