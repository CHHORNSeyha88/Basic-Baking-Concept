package seyha.web.app.Bank_Concepts.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seyha.web.app.Bank_Concepts.entity.*;
import seyha.web.app.Bank_Concepts.repository.TransactionsRepository;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionsRepository transactionsRepository;



     @Transactional
    public Transactions createAccountTransactions(double amount, Type type, double txFee, User user, Account account) {
        var tx = Transactions.builder()
                .txFee(txFee)
                .amount(amount)
                .type(type)
                .status(Status.COMPLETED)
                .owner(user)
                .account(account)
                .build();
        return transactionsRepository.save(tx);
    }

}
