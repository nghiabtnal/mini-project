package org.example.miniproject.dao.mongo;

import lombok.extern.slf4j.Slf4j;
import org.example.miniproject.api.account.dto.AccountDTO;
import org.example.miniproject.repositories.AccountRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

//@Component()
@Slf4j
public class AccountDaoMongo implements AccountRepository {

    @Override
    public void create(AccountDTO dto) {
        //TODO: applying create logic
    }

    @Override
    public Optional<AccountDTO> get(Integer id) {
        return Optional.empty();
    }
}
