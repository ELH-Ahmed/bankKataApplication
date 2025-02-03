package com.example.bankkata.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private double balance = 0;
    private final List<Transaction> transactions = new ArrayList<>();

    public Transaction deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        balance += amount;
        Transaction transaction = new Transaction(java.time.LocalDateTime.now(), amount, balance, TransactionType.DEPOSIT);
        transactions.add(transaction);
        return transaction;
    }

    public Transaction withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        balance -= amount;
        Transaction transaction = new Transaction(java.time.LocalDateTime.now(), amount, balance, TransactionType.WITHDRAWAL);
        transactions.add(transaction);
        return transaction;
    }

    public List<Transaction> getStatement() {
        return transactions;
    }
}
