package io.alecbrando.alecbrando.datasource.mock

import io.alecbrando.alecbrando.datasource.BankDataSource
import io.alecbrando.alecbrando.models.Bank
import org.springframework.stereotype.Repository

@Repository
class MockBankDataSource : BankDataSource {

    val banks = mutableListOf(
        Bank("123", 17, 3.0),
        Bank("1234", 17, 1.0),
        Bank("12345", 12, 3.0),
        Bank("123456", 124, 31.0),
    )

    override fun retrieveBanks(): Collection<Bank> {
        return banks
    }

    override fun retrieveBank(accountNumber: String): Bank {
        return banks.firstOrNull() { it.accountNumber == accountNumber }
            ?: throw NoSuchElementException("Could not find a bank with account number $accountNumber")
    }

    override fun addBank(bank: Bank): Bank {
        if (banks.contains(bank)) throw IllegalArgumentException("Bank Account already exists")
        banks.add(bank)
        return bank
    }

    override fun updateBank(bank: Bank): Bank {
        banks.forEachIndexed { index, it ->
            if (it.accountNumber == bank.accountNumber) {
                banks[index] = bank
            } else if (index == (banks.size - 1)) {
                throw NoSuchElementException("Could not find a bank with account number ${bank.accountNumber}")
            }
        }
        return bank
    }

    override fun deleteBank(accountNumber: String) {
        val bank = banks.firstOrNull() { it.accountNumber == accountNumber }
            ?: throw NoSuchElementException("The bank doesn't exist for account number $accountNumber")
        banks.remove(bank)

    }
}