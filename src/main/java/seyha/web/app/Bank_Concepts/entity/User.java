package seyha.web.app.Bank_Concepts.entity;
import seyha.web.app.Bank_Concepts.entity.Card;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "bank_users")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uid;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private Date dob;

    @Column(nullable = false)
    private long tel;

    private String tag;

    @Column(nullable = false)
    private String password;

    private String gender;

    @CreationTimestamp
    private LocalDateTime createAt;
    @UpdateTimestamp
    private LocalDateTime updateAt;





    @OneToOne(mappedBy = "owner")
    @JsonIgnore
    private Card card;

    @OneToMany(mappedBy = "owner",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Transactions> transactions;

    @OneToMany(mappedBy = "owner",cascade= CascadeType.ALL,fetch =FetchType.LAZY)
    @JsonIgnore
    private List<Account> accounts;


}