package io.alecbrando.alecbrando.service

import io.alecbrando.alecbrando.datasource.BankDataSource
import io.alecbrando.alecbrando.models.Bank
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping

@Service
class BankService(
    private val dataSource: BankDataSource
) {
    fun getBanks(): Collection<Bank> {
        return dataSource.retrieveBanks()
    }

    fun getBank(accountNumber: String): Bank {
        return dataSource.retrieveBank(accountNumber)
    }

    fun addBank(bank: Bank): Bank {
        return dataSource.addBank(bank)
    }

    fun updateBank(bank: Bank): Bank {
        return dataSource.updateBank(bank)
    }

    fun deleteBank(accountNumber: String) {
        return dataSource.deleteBank(accountNumber)
    }
}