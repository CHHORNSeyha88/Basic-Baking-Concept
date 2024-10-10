package seyha.web.app.Bank_Concepts.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import seyha.web.app.Bank_Concepts.entity.Transactions;

import java.util.List;
import java.util.Optional;

public interface TransactionsRepository extends JpaRepository<Transactions,String> {

    Page<Transactions> findAllByOwnerUid(String uid, Pageable pageable);

    Page<Transactions> findAllByCardCardIdAndOwnerUid(String cardId, String uid, Pageable pageable);

    Page<Transactions> findAllByAccountAccountIdAndOwnerUid(String accountId, String uid, Pageable pageable);
}
