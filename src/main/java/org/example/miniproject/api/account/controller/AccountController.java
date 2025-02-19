package org.example.miniproject.api.account.controller;

import lombok.RequiredArgsConstructor;
import org.example.miniproject.api.account.dto.AccountDTO;
import org.example.miniproject.service.AccountService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public void search(@RequestBody AccountDTO dto) {
        accountService.create(dto);
    }

    @GetMapping(value = "/{id:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountDTO detail(@PathVariable Integer id) {
        return accountService.get(id);
    }
}
