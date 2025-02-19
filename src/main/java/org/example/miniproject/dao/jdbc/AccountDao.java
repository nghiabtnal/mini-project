package org.example.miniproject.dao.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.miniproject.api.account.dto.AccountDTO;
import org.example.miniproject.repositories.AccountRepository;
import org.example.miniproject.repositories.generic.ModifyingRepository;
import org.example.miniproject.repositories.generic.ReadOnlyRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccountDao implements AccountRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void create(AccountDTO dto) {
        String query = "INSERT INTO account (id, name, email) VALUES (:accountId, :accountName, :email)";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("accountId", dto.getId());
        parameters.addValue("accountName", dto.getName());
        parameters.addValue("email", dto.getEmail());
        jdbcTemplate.update(query, parameters);
    }

    @Override
    public Optional<AccountDTO> get(Integer id) {
        RowMapper<AccountDTO> mapper = new BeanPropertyRowMapper<>(AccountDTO.class);
        String query = "SELECT a.id, a.name, a.email " +
                "FROM account a WHERE a.id = :accountId";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("accountId", id);
        List<AccountDTO> accountList = this.jdbcTemplate.query(query, parameters, mapper);
        if (CollectionUtils.isEmpty(accountList)) {
            return Optional.empty();
        }
        return Optional.of(accountList.get(0));
    }
}
