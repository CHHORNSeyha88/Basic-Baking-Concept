package seyha.web.app.Bank_Concepts.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import seyha.web.app.Bank_Concepts.entity.Transactions;
import seyha.web.app.Bank_Concepts.entity.User;
import seyha.web.app.Bank_Concepts.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

//    @GetMapping
//    public List<Transactions> getAllTransactions(@RequestParam String page, Authentication auth) {
//        return transactionService.getAllTransactions(page, (User) auth.getPrincipal());
//    }

}
