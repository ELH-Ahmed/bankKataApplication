package com.example.bankkata.application.service;

import com.example.bankkata.domain.model.Account;
import com.example.bankkata.domain.model.Transaction;
import com.example.bankkata.domain.port.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Transaction deposit(double amount) {
        Account account = accountRepository.getAccount();
        Transaction transaction = account.deposit(amount);
        accountRepository.save(account);
        return transaction;
    }

    public Transaction withdraw(double amount) {
        Account account = accountRepository.getAccount();
        Transaction transaction = account.withdraw(amount);
        accountRepository.save(account);
        return transaction;
    }

    public List<Transaction> getStatement() {
        return accountRepository.getAccount().getStatement();
    }
}