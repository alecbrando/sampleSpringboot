package io.alecbrando.alecbrando.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.alecbrando.alecbrando.models.Bank
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.result.ContentResultMatchers
import org.springframework.web.client.HttpClientErrorException.NotFound

@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest @Autowired constructor(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper
) {
    private val baseUrl = "/api/banks"

    @Nested
    @DisplayName("get /api/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBanks() {
        @Test
        fun `should return all banks`() {
            mockMvc.get(baseUrl).andDo {
                print()
            }.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$[0].accountNumber") {
                    value("123")
                }
            }
        }
    }

    @Nested
    @DisplayName("get /api/banks/{accountNumber}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBank {
        @Test
        fun `should return the bank with the given account number`() {
            // given
            val accountNumber = 123
            // when
            mockMvc.get("$baseUrl/$accountNumber")
                // then
                .andDo { println() }
                .andExpect { status { isOk() } }
                .andExpect {
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.accountNumber") {
                        value("123")
                    }
                }
        }

        @Test
        fun `should return not found if the account number does not exist`() {
            // given
            val accountNumber = "does_not_exist"

            // when
            // then
            mockMvc.get("$baseUrl/$accountNumber")
                .andDo { println() }
                .andExpect {
                    status { isNotFound() }
                }
        }
    }

    @Nested
    @DisplayName("post /api/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class AddBank() {
        @Test
        fun `should add the new bank`() {
            // given
            val newBank = Bank("acc123", 2, 31.5)

            // when
            mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newBank)
            }

                // then
                .andDo { println() }
                .andExpect {
                    status { isCreated() }
                    jsonPath("$.accountNumber") {
                        value(newBank.accountNumber)
                    }
                }
        }

        @Test
        fun `should not add a bank account that already exists BAD REQUEST`() {
            // given
            val invalidBank = Bank("123", 17, 3.0)

            // when
            mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }

                // then
                .andDo { println() }
                .andExpect {
                    status { isBadRequest() }
                }
        }
    }

    @Nested
    @DisplayName("PATCH /api/bank")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PatchExistingBank {
        @Test
        fun `should update an existing bank`() {
            // given
            val updatedBank = Bank("1234", 1, 1.0)
            // when
            mockMvc.patch(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updatedBank)
            }
                // then
                .andDo {
                    println()
                }
                .andExpect {
                    status {
                        isOk()
                    }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(updatedBank))
                    }
                }
        }

        @Test
        fun `should return with a BAD request when trying to update a bank that doesn't exist`() {
            // given
            val updatedBank = Bank("INVALID_NUMBER", 1, 1.0)
            // when
            mockMvc.patch(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updatedBank)
            }
                // then
                .andDo {
                    println()
                }
                .andExpect {
                    status {
                        isNotFound()
                    }
                }
        }
    }

    @Nested
    @DisplayName("DELETE /api/banks/{accountNumber}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteBank {
        @Test
        @DirtiesContext
        fun `should delete a bank and return ok`() {
            // given
            val bank = Bank("123", 17, 3.0)

            // when
            mockMvc.delete("$baseUrl/${bank.accountNumber}")
                .andDo {
                    println()
                }
                // then
                .andExpect {
                    status { isNoContent() }
                }
            mockMvc.get("$baseUrl/${bank.accountNumber}")
                .andDo {
                    println()
                }.andExpect {
                    status { isNotFound() }
                }
        }

        @Test
        fun `should return not found if no bank with given account number exists`() {
            // given
            val tobeDeletedBank = Bank("INVALID_NUMBER", 1, 1.0)
            // when
            mockMvc.delete("$baseUrl/${tobeDeletedBank.accountNumber}")
                // then
                .andDo {
                    println()
                }
                .andExpect {
                    status {
                        isNotFound()
                    }
                }
        }
    }
}