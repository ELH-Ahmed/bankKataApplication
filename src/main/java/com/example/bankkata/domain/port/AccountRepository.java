package com.example.bankkata.domain.port;

import com.example.bankkata.domain.model.Account;

public interface AccountRepository {
    Account getAccount();
    void save(Account account);
}
