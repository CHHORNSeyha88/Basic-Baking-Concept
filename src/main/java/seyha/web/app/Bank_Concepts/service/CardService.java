package seyha.web.app.Bank_Concepts.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seyha.web.app.Bank_Concepts.entity.Card;
import seyha.web.app.Bank_Concepts.entity.Transactions;
import seyha.web.app.Bank_Concepts.entity.Type;
import seyha.web.app.Bank_Concepts.entity.User;
import seyha.web.app.Bank_Concepts.repository.CardRepository;
import seyha.web.app.Bank_Concepts.service.assist.AccountAsistant;
import seyha.web.app.Bank_Concepts.utils.RandomUtil;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final AccountAsistant accountAsistant;
    private final TransactionService transactionService;

    public Card getCard(User user) {
        return cardRepository.findByOwnerUid(user.getUid()).orElseThrow();
    }

    public long createCard(double amount, User user) throws Exception{
        if (amount < 2) {
            throw new IllegalArgumentException("Invalid card amount, must be at least 2$ up to create ");
        }
        if (!accountAsistant.existsByCodeAndOwnerUid("USD", user.getUid())) {
            throw new IllegalArgumentException("USD Account not found for this user, so the card cannot be created");
        }
        var usdAccount = accountAsistant.findByCodeAndOwnerUid("USD", user.getUid()).orElseThrow();
        accountAsistant.ValidateSufficientFunds(usdAccount, amount);
        usdAccount.setBalance(usdAccount.getBalance() - amount);

        long cardNumber;
        do {
            cardNumber = generateCardNumber();
        } while (cardRepository.existsCardByCardNumber(cardNumber));

        Card card = Card.builder()
                .cardNumber(cardNumber) // Ensure card number is stored
                .cardHolderName(user.getFirstname() + " " + user.getLastname())
                .cardExpiryDate(LocalDateTime.now().plusYears(3))
                .cardType("Visa")
                .owner(user)
                .cvv(new RandomUtil().generateRandom(3)) // No need for toString(), generateRandom should return a String
                .balance(amount - 1) // Deduct 1$ for card creation
                .build();
        card = cardRepository.save(card);

        transactionService.createAccountTransactions(1, Type.WITHDRAW, 0.00, user, usdAccount); // Card creation fee
        transactionService.createAccountTransactions(amount - 1, Type.WITHDRAW, 0.00, user, usdAccount); // Card funding
        transactionService.createCardTransaction(amount - 1, Type.WITHDRAW, 0.00, user, card);

        accountAsistant.save(usdAccount);

        return cardNumber; // Return the card number instead of the card object
    }

    private long generateCardNumber() {
        return new RandomUtil().generateRandom(10); // Generate 10 random digits and convert to long
    }

    public Transactions creditCard(double amount, User user) {
        var usdAccount = accountAsistant.findByCodeAndOwnerUid("USD", user.getUid()).orElseThrow();
        usdAccount.setBalance(usdAccount.getBalance() + amount); // Add amount to account balance
        transactionService.createAccountTransactions(amount, Type.DEPOSIT, 0.00, user, usdAccount); // Log account deposit

        var card = user.getCard();
        card.setBalance(card.getBalance() + amount); // Increase card balance
        cardRepository.save(card); // Save updated card

        return transactionService.createCardTransaction(amount, Type.DEPOSIT, 0.00, user, card); // Log card transaction
    }

    public Transactions debitCard(double amount, User user) {
        var usdAccount = accountAsistant.findByCodeAndOwnerUid("USD", user.getUid()).orElseThrow();
        usdAccount.setBalance(usdAccount.getBalance() - amount); // Deduct amount from account balance
        transactionService.createAccountTransactions(amount, Type.WITHDRAW, 0.00, user, usdAccount); // Log account withdrawal

        var card = user.getCard();
        card.setBalance(card.getBalance() - amount); // Decrease card balance
        cardRepository.save(card); // Save updated card

        return transactionService.createCardTransaction(amount, Type.DEBIT, 0.00, user, card); // Log card transaction
    }
}
