package com.example.bankkata.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
@Getter
@RequiredArgsConstructor
public class Transaction {
    private final LocalDateTime date;
    private final double amount;
    private final double newBalance;
    private final TransactionType transactionType;
}
