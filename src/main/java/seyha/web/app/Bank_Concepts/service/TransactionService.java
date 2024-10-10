package seyha.web.app.Bank_Concepts.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seyha.web.app.Bank_Concepts.entity.*;
import seyha.web.app.Bank_Concepts.repository.TransactionsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionsRepository transactionsRepository;


    public Page<Transactions> getAllTransactions(String page, User user) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page),10, Sort.by("createdAt").ascending());
        return transactionsRepository.findAllByOwnerUid(user.getUid(),pageable);

    }

    public List<Transactions> getTransactionsByCardId(String cardId, String page, User user) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10, Sort.by("createdAt").ascending());
        return transactionsRepository.findAllByCardCardIdAndOwnerUid(cardId, user.getUid(), pageable).getContent();
    }
    public List<Transactions> getTransactionsByAccountId(String accountId, String page, User user) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10, Sort.by("createdAt").ascending());
        return transactionsRepository.findAllByAccountAccountIdAndOwnerUid(accountId, user.getUid(), pageable).getContent();
    }


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

    public Transactions createCardTransaction(double amount, Type type, double textFee, User user, Card card) {
        var tx = Transactions.builder()
                .txFee(textFee)
                .amount(amount)
                .type(type)
                .card(card)
                .status(Status.COMPLETED)
                .owner(user)
                .build();
        return transactionsRepository.save(tx);
    }
}
