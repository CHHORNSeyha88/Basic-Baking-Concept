package seyha.web.app.Bank_Concepts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferDto {
    /**
     * The account number of the recipient.
     */
    private long recipientAccountNumber;

    /**
     * The amount to be transferred.
     */
    private double amount;

    /**
     * The unique code for the transfer operation.
     */
    private String code;
}
