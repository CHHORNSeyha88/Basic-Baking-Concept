package seyha.web.app.Bank_Concepts.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String txId;

    /**
     * The amount of money involved in the transaction.
     */
    private Double amount;

    /**
     * The transaction fee associated with the transaction.
     */
    private Double txFee;

    /**
     * The identifier of the sender of the transaction.
     */
    private String sender;

    /**
     * The identifier of the receiver of the transaction.
     */
    private String receiver;

    /**
     * The timestamp of the last update to the transaction.
     */
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /**
     * The timestamp of the creation of the transaction.
     */
    @CreationTimestamp
    private LocalDateTime createdAt;

    /**
     * The status of the transaction.
     */
    @Enumerated(value = EnumType.STRING)
    private Status status;

    /**
     * The type of the transaction.
     */
    @Enumerated(value = EnumType.STRING)
    private Type type;


    /**
     * The card associated with the transaction.
     */

    @ManyToOne
    @JoinColumn(name = "card_id")
    @JsonIgnore
    private Card card;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private User owner;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;


}
