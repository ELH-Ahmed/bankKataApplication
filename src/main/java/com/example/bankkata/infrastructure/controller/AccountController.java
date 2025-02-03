package com.example.bankkata.infrastructure.controller;

import com.example.bankkata.application.service.AccountService;
import com.example.bankkata.domain.model.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestParam double amount) {
        Transaction transaction = accountService.deposit(amount);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@RequestParam double amount) {
        Transaction transaction = accountService.withdraw(amount);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/statement")
    public ResponseEntity<List<Transaction>> getStatement() {
        List<Transaction> transactions = accountService.getStatement();
        return ResponseEntity.ok(transactions);
    }
}
