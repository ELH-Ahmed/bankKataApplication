package com.example.bankkata.infrastructure.adapter;

import com.example.bankkata.domain.model.Account;
import com.example.bankkata.domain.port.AccountRepository;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryAccountRepository implements AccountRepository {

    private Account account = new Account();

    @Override
    public Account getAccount() {
        return account;
    }

    @Override
    public void save(Account account) {
        this.account = account;
    }
}
