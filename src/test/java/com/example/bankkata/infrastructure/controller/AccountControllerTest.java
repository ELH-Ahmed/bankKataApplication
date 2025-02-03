package com.example.bankkata.infrastructure.controller;

import com.example.bankkata.application.service.AccountService;
import com.example.bankkata.domain.model.Transaction;
import com.example.bankkata.domain.model.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private Transaction depositTransaction;
    private Transaction withdrawalTransaction;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();

        depositTransaction = new Transaction(LocalDateTime.now(), 200.0, 1200.0, TransactionType.DEPOSIT);
        withdrawalTransaction = new Transaction(LocalDateTime.now(), 100.0, 1100.0, TransactionType.WITHDRAWAL);
    }

    @Test
    void deposit_ShouldReturnTransaction() throws Exception {
        double amountToDeposit = 200.0;
        when(accountService.deposit(amountToDeposit)).thenReturn(depositTransaction);

        mockMvc.perform(post("/account/deposit")
                        .param("amount", "200.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(200.0))
                .andExpect(jsonPath("$.newBalance").value(1200.0))
                .andExpect(jsonPath("$.transactionType").value("DEPOSIT"));

        verify(accountService).deposit(amountToDeposit);
    }


    @Test
    void withdraw_ShouldReturnTransaction() throws Exception {
        double amountToWithdraw = 100.0;
        when(accountService.withdraw(amountToWithdraw)).thenReturn(withdrawalTransaction);

        mockMvc.perform(post("/account/withdraw")
                        .param("amount", "100.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.newBalance").value(1100.0))
                .andExpect(jsonPath("$.transactionType").value("WITHDRAWAL"));

        verify(accountService).withdraw(amountToWithdraw);
    }

    @Test
    void getStatement_ShouldReturnTransactionList() throws Exception {
        List<Transaction> transactions = List.of(depositTransaction, withdrawalTransaction);
        when(accountService.getStatement()).thenReturn(transactions);

        mockMvc.perform(get("/account/statement")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].transactionType").value("DEPOSIT"))
                .andExpect(jsonPath("$[1].transactionType").value("WITHDRAWAL"));

        verify(accountService).getStatement();
    }
}
