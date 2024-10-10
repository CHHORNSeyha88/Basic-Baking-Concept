package seyha.web.app.Bank_Concepts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seyha.web.app.Bank_Concepts.entity.Card;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card,String> {

    Optional<Card> findByOwnerUid(String uid);

    boolean existsCardByCardNumber(double cardNumber);
}
