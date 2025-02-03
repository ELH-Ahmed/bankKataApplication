package com.example.bankkata.infrastructure.adapter;

import com.example.bankkata.domain.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryAccountRepositoryTest {

    private InMemoryAccountRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAccountRepository();
    }

    @Test
    void getAccount_ShouldReturnSameAccountInstance() {
        Account account1 = repository.getAccount();
        Account account2 = repository.getAccount();

        assertNotNull(account1);
        assertSame(account1, account2, "The repository should always return the same account instance");
    }

    @Test
    void save_ShouldUpdateAccountReference() {
        Account newAccount = new Account();
        repository.save(newAccount);

        assertSame(newAccount, repository.getAccount(), "The repository should update to the new account instance");
    }
}
