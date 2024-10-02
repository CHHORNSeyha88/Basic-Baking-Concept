package seyha.web.app.Bank_Concepts.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cards")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String cardId;

    @Column(nullable = false, unique = true)
    private long cardNumber;

    private String cardHolderName;

    private String cardType;

    private String cvv;

    private Double balance;
    private String pin;
    /**
     * Billing address associated with the card.
     */
    private String billingAddress;

    /**
     * Date of issuance.
     */

    @CreationTimestamp
    private LocalDate iss;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime cardExpiryDate;

    @OneToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL,fetch =FetchType.LAZY)
     private List<Transactions> transactions;






}
