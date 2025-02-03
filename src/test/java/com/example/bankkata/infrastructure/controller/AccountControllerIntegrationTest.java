package com.example.bankkata.infrastructure.controller;

import com.example.bankkata.application.service.AccountService;
import com.example.bankkata.domain.model.Account;
import com.example.bankkata.domain.port.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository.save(new Account());
        accountService.deposit(150.0);
        accountService.deposit(50.0);
    }

    @Test
    void testDeposit() throws Exception {
        double depositAmount = 30.0;
        mockMvc.perform(post("/account/deposit")
                        .param("amount", String.valueOf(depositAmount))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.amount").value(depositAmount))
                .andExpect(jsonPath("$.newBalance").value(230.0))
                .andExpect(jsonPath("$.transactionType").value("DEPOSIT"));
    }

    @Test
    void testWithdraw() throws Exception {
        double withdrawalAmount = 50.0;
        mockMvc.perform(post("/account/withdraw")
                        .param("amount", String.valueOf(withdrawalAmount))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.amount").value(withdrawalAmount))
                .andExpect(jsonPath("$.newBalance").value(150.0))
                .andExpect(jsonPath("$.transactionType").value("WITHDRAWAL"));
    }

    @Test
    void testGetStatement() throws Exception {
        double depositAmount = 100.0;
        double withdrawalAmount = 50.0;

        accountService.deposit(depositAmount);
        accountService.withdraw(withdrawalAmount);

        mockMvc.perform(get("/account/statement")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[-2].amount").value(depositAmount))
                .andExpect(jsonPath("$[-1].amount").value(withdrawalAmount));
    }

    @Test
    void testDepositExceptionHandling() throws Exception {
        double invalidAmount = -100.0;
        mockMvc.perform(post("/account/deposit")
                        .param("amount", String.valueOf(invalidAmount))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Deposit amount must be positive"));
    }

    @Test
    void testWithdrawExceptionHandling() throws Exception {
        double invalidAmount = 1000.0;
        mockMvc.perform(post("/account/withdraw")
                        .param("amount", String.valueOf(invalidAmount))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Insufficient balance"));
    }
}