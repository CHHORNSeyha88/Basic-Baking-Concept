package seyha.web.app.Bank_Concepts.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    /**
     * The unique identifier or code of the account.
     */
    private String code;

    /**
     * The descriptive label or name of the account.
     */
    private String label;

    /**
     * The symbol or abbreviation representing the account.
     */
    private char symbol;
}
