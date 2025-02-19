package org.example.miniproject.service;

import org.example.miniproject.api.account.dto.AccountDTO;
import org.example.miniproject.models.Account;

public interface AccountService {
    void create(AccountDTO accountDTO);
    AccountDTO get(Integer id);
}
