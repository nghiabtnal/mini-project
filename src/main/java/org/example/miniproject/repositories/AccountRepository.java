package org.example.miniproject.repositories;

import org.example.miniproject.api.account.dto.AccountDTO;
import org.example.miniproject.repositories.generic.ModifyingRepository;
import org.example.miniproject.repositories.generic.ReadOnlyRepository;

public interface AccountRepository
        extends ModifyingRepository<AccountDTO>, ReadOnlyRepository<AccountDTO, Integer> {
}
