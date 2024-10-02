package seyha.web.app.Bank_Concepts.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    /**
     * Unique identifier for the account.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String accountId;


    private double balance;

    /**
     * Name of the account.
     */
    private String accountName;

    /**
     * Unique account number.
     */
    @Column(unique = true, nullable = false)
    private long accountNumber;

    /**
     * Currency of the account.
     */
    private String currency;

    /**
     * Code associated with the account.
     */
    private String code;

    /**
     * Label for the account.
     */
    private String label;

    /**
     * Symbol representing the account.
     */
    private char symbol;


    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private User owner;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transactions> transactions;




}
