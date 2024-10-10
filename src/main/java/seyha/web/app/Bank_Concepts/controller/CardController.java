package seyha.web.app.Bank_Concepts.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import seyha.web.app.Bank_Concepts.entity.Card;
import seyha.web.app.Bank_Concepts.entity.Transactions;
import seyha.web.app.Bank_Concepts.entity.User;
import seyha.web.app.Bank_Concepts.service.CardService;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @GetMapping
    public ResponseEntity<Card> getCard(Authentication authentication){
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cardService.getCard(user));
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createCard(@RequestParam double amount, Authentication authentication) throws Exception {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cardService.createCard(amount, user));
    }

    @PostMapping("/debit")
    public ResponseEntity<Transactions> debitCard(@RequestParam double amount, Authentication authentication){
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cardService.debitCard(amount, user));
    }

}
