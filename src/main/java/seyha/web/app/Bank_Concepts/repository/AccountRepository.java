package seyha.web.app.Bank_Concepts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seyha.web.app.Bank_Concepts.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,String> {
   boolean existsAccountByAccountNumber(Long accountNumber);


    List<Account> findAllByOwnerUid(String uid);

    boolean existsByCodeAndOwnerUid(String code, String uid);

    Optional<Account> findByCodeAndOwnerUid(String code, String uid);

    Optional<Account> findByAccountNumber(long recipientAccountNumber);
}
