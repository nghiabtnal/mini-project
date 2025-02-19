package org.example.miniproject.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.miniproject.api.account.dto.AccountDTO;
import org.example.miniproject.libs.datasource.ReadOnlyDataSource;
import org.example.miniproject.libs.exceptions.NotFoundException;
import org.example.miniproject.repositories.AccountRepository;
import org.example.miniproject.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(AccountDTO dto) {
        accountRepository.create(dto);
    }

    @Override
    @ReadOnlyDataSource
    @Transactional(readOnly = true)
    public AccountDTO get(Integer id) {
        return accountRepository.get(id).orElseThrow(() -> new NotFoundException());
    }
}
