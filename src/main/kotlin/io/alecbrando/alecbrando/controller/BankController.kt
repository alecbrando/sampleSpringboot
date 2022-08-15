package io.alecbrando.alecbrando.controller

import io.alecbrando.alecbrando.models.Bank
import io.alecbrando.alecbrando.service.BankService
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/banks")
class BankController(
    private val bankService: BankService
) {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleAlreadyExist(e: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.BAD_REQUEST)

    @GetMapping
    fun retrieveAllBanks(): Collection<Bank> {
        return bankService.getBanks()
    }

    @GetMapping("/{accountNumber}")
    fun retrieveBank(@PathVariable accountNumber: String): Bank {
        return bankService.getBank(accountNumber)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addBank(@RequestBody bank: Bank): Bank {
        return bankService.addBank(bank)
    }

    @PatchMapping
    fun updateBank(@RequestBody bank: Bank): Bank {
        return bankService.updateBank(bank)
    }

    @DeleteMapping("/{accountNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBank(@PathVariable accountNumber: String) {
        bankService.deleteBank(accountNumber)
    }
}