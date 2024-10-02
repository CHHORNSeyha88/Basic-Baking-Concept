package seyha.web.app.Bank_Concepts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seyha.web.app.Bank_Concepts.entity.Account;

public interface AccountRepository extends JpaRepository<Account,String> {
}
