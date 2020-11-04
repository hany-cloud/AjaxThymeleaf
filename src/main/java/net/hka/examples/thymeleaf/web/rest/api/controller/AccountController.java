package net.hka.examples.thymeleaf.web.rest.api.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.hka.examples.thymeleaf.web.dto.AccountDto;
import net.hka.examples.thymeleaf.web.service.AccountService;

@RestController
class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("account/current")
    @ResponseStatus(value = HttpStatus.OK)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    Optional<AccountDto> currentAccount(Principal principal) {
        //Assert.notNull(principal);
        return accountService.findOneByEmail(principal.getName());
    }

    @GetMapping("account/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    Optional<AccountDto> account(@PathVariable("id") Long id) {
        return accountService.findById(id);
    }
    
}
