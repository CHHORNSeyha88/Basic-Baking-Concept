package seyha.web.app.Bank_Concepts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seyha.web.app.Bank_Concepts.entity.Transactions;

public interface TransactionsRepository extends JpaRepository<Transactions,String> {
}
