package seyha.web.app.Bank_Concepts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seyha.web.app.Bank_Concepts.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,String> {
   boolean existsAccountByAccountNumber(Long accountNumber);


    @Query("SELECT DISTINCT a FROM Account a WHERE a.owner.uid = :uid")
    List<Account> findAllByOwnerUid(@Param("uid") String uid);

    boolean existsByCodeAndOwnerUid(String code, String uid);

    Optional<Account> findByCodeAndOwnerUid(String code, String uid);

    Optional<Account> findByAccountNumber(long recipientAccountNumber);
}
