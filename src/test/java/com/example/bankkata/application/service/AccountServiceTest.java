package com.example.bankkata.application.service;

import com.example.bankkata.domain.model.Account;
import com.example.bankkata.domain.model.Transaction;
import com.example.bankkata.domain.model.TransactionType;
import com.example.bankkata.domain.port.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private Account account;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(accountRepository.getAccount()).thenReturn(account);
    }

    @Test
    void deposit_ShouldCreateTransactionAndUpdateBalance() {
        double amount = 200.0;
        double newBalance = 1200.0;
        Transaction mockTransaction = new Transaction(LocalDateTime.now(), amount, newBalance, TransactionType.DEPOSIT);

        when(account.deposit(amount)).thenReturn(mockTransaction);

        Transaction result = accountService.deposit(amount);

        assertNotNull(result);
        assertEquals(amount, result.getAmount());
        assertEquals(newBalance, result.getNewBalance());
        assertEquals(TransactionType.DEPOSIT, result.getTransactionType());

        verify(account).deposit(amount);
        verify(accountRepository).save(account);
    }

    @Test
    void withdraw_ShouldCreateTransactionAndUpdateBalance() {
        double amount = 100.0;
        double newBalance = 900.0;
        Transaction mockTransaction = new Transaction(LocalDateTime.now(), amount, newBalance, TransactionType.WITHDRAWAL);

        when(account.withdraw(amount)).thenReturn(mockTransaction);

        Transaction result = accountService.withdraw(amount);

        assertNotNull(result);
        assertEquals(amount, result.getAmount());
        assertEquals(newBalance, result.getNewBalance());
        assertEquals(TransactionType.WITHDRAWAL, result.getTransactionType());

        verify(account).withdraw(amount);
        verify(accountRepository).save(account);
    }

    @Test
    void getStatement_ShouldReturnAllTransactions() {
        List<Transaction> mockTransactions = List.of(
                new Transaction(LocalDateTime.now(), 1100.0, 1100.0, TransactionType.DEPOSIT),
                new Transaction(LocalDateTime.now(), 50.0, 1050.0, TransactionType.WITHDRAWAL)
        );

        when(account.getStatement()).thenReturn(mockTransactions);

        List<Transaction> result = accountService.getStatement();

        assertEquals(2, result.size());
        assertEquals(1100.0, result.get(0).getAmount());
        assertEquals(50.0, result.get(1).getAmount());
        assertEquals(TransactionType.DEPOSIT, result.get(0).getTransactionType());
        assertEquals(TransactionType.WITHDRAWAL, result.get(1).getTransactionType());

        verify(accountRepository).getAccount();
    }
}