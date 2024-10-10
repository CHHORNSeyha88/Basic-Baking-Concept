package seyha.web.app.Bank_Concepts.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import seyha.web.app.Bank_Concepts.dto.AccountDto;
import seyha.web.app.Bank_Concepts.dto.ConvertDto;
import seyha.web.app.Bank_Concepts.dto.TransferDto;
import seyha.web.app.Bank_Concepts.entity.Account;
import seyha.web.app.Bank_Concepts.entity.Transactions;
import seyha.web.app.Bank_Concepts.entity.User;
import seyha.web.app.Bank_Concepts.service.AccountService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> createAccount (@RequestBody AccountDto accountDto, Authentication authentication) throws Exception {

        var user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(accountService.createAccount(accountDto, user));

    }

    @GetMapping
    public ResponseEntity<List<Account>> getUserAccounts (Authentication authentication) throws Exception {

        var user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(accountService.getUserAccounts(user.getUid()));

    }

    @PostMapping("/transfer")
    public ResponseEntity<Transactions> transferFunds (@RequestBody TransferDto transferDto, Authentication authentication) throws Exception {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.transferFunds(transferDto, user));
    }

    @GetMapping("/rates")
    public ResponseEntity<Map<String,Double>> getExchangeRates() {
        return ResponseEntity.ok(accountService.getExchangeRate());
    }

    @PostMapping("/convert")
    public ResponseEntity<Transactions> convertCurrency(@RequestBody ConvertDto convertDto, Authentication authentication) throws Exception {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.getConvertCurrency(convertDto, user));
    }
}
