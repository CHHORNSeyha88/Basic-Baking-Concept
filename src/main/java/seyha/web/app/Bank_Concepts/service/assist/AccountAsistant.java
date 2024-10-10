package seyha.web.app.Bank_Concepts.service.assist;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import seyha.web.app.Bank_Concepts.dto.AccountDto;
import seyha.web.app.Bank_Concepts.dto.ConvertDto;
import seyha.web.app.Bank_Concepts.entity.*;
import seyha.web.app.Bank_Concepts.repository.AccountRepository;
import seyha.web.app.Bank_Concepts.repository.TransactionsRepository;
import seyha.web.app.Bank_Concepts.service.ExchangeRatesService;
import seyha.web.app.Bank_Concepts.service.TransactionService;
import seyha.web.app.Bank_Concepts.utils.RandomUtil;

import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Getter
@RequiredArgsConstructor
public class AccountAsistant {

    private final TransactionsRepository transactionRepository;
    private final ExchangeRatesService exchangeRatesService;

    private final AccountRepository accountRepository;
    private final Map<String,String> CURRENTCIES = Map.of(
            "USD","United States dollar"
            ,"KHR","Cambodia Riel"
            ,"CNY","Chinese Yuan"
            ,"EUR","Euro"
            ,"GBP","British Pound"
            ,"JPY","Japanese Yen"
            ,"CHF","Swiss Franc"
            ,"CAD","Canadian Dollar"
            ,"AUD","Australian Dollar"
            ,"NZD","New Zealand Dollar"
    );
    private final TransactionService transactionService;

    public Account createAccount(AccountDto accountDto, User user) throws Exception {
        long accountNum;
        validateAccountNonExist(accountDto.getCode(), user.getUid());
        do {
            accountNum = new RandomUtil().generateRandom(10);


        }while (accountRepository.existsAccountByAccountNumber(accountNum));
        var account = Account.builder()
                .accountNumber(accountNum)
                .accountName(user.getFirstname()+" "+user.getLastname())
                .balance(1000)
                .owner(user)
                .code(accountDto.getCode())
                .symbol(accountDto.getSymbol())
                .label(CURRENTCIES.get(accountDto.getCode()))
                .build();
        accountRepository.save(account);
        return account;
    }

    public void validateAccountNonExist(String code, String uid) throws Exception {
        if(accountRepository.existsByCodeAndOwnerUid(code, uid)){
            throw new Exception("Account of this type Exists and this user");
        }
    }

    //UTF8 if erorr use this

    public String sanitizeString(String input) {
        if (input == null) {
            return null;
        }
        // Remove null byte if present
        return input.replaceAll("\u0000", "");
    }


    public Transactions transferPerformance (Account senderAccount, Account receiverAccount, double amount,User user) throws OperationNotSupportedException {
        ValidateSufficientFunds(senderAccount,(amount*1.01));
        senderAccount.setBalance(senderAccount.getBalance()-amount *1.01);
        receiverAccount.setBalance(receiverAccount.getBalance()+amount);
        accountRepository.saveAll(List.of(senderAccount, receiverAccount));
        var senderTransaction = transactionService.createAccountTransactions(amount,Type.WITHDRAW,amount*1.01,user,senderAccount);
        var recipientAccountNumber = transactionService.createAccountTransactions(amount,Type.DEPOSIT,0.00,receiverAccount.getOwner(),receiverAccount);

      return senderTransaction;
    }


    public void ValidateAccountOwner(Account owner,User user) throws Exception {

        if(!owner.getOwner().getUid().equals(user.getUid())){
            throw new OperationNotSupportedException("Invalid account owner");
        }

    }

    public void ValidateSufficientFunds(Account account, double amount) throws OperationNotSupportedException {
        if(account.getBalance()<amount){
            throw new OperationNotSupportedException("Insufficient funds in this account");
        }
    }

     public void ValidateAmount(double amount) throws OperationNotSupportedException{
        if(amount <= 0){
            throw new IllegalArgumentException("Invalid Amount");
        }
     }
    /**
     * Validates that the provided currencies for conversion are different.
     *
     * @param convertDto The DTO containing the currencies to be validated.
     * @throws IllegalArgumentException If the currencies are the same.
     */

     public void ValidateDifferentCurrencyType(ConvertDto convertDto) throws Exception {
        if(convertDto.getToCurrency().equals(convertDto.getFromCurrency())){
            throw new IllegalArgumentException("Conversion between the same currency type not allow");
        }
     }

    /**
     * Validates that the provided user is the owner of both the sender and receiver accounts involved in a currency conversion.
     *
     * @param convertDto The DTO containing the currencies to be validated.
     * @param uid The unique identifier of the user for whom the account ownership is being validated.
     * @throws Exception If the provided user is not the owner of either the sender or receiver account.
     */

     public void ValidateAccountOwnerShip(ConvertDto convertDto,String uid) throws Exception {
         accountRepository.findByCodeAndOwnerUid(convertDto.getFromCurrency(), uid).orElseThrow();
         accountRepository.findByCodeAndOwnerUid(convertDto.getToCurrency(), uid).orElseThrow();
     }
    /**
     * Validates that the provided user is the owner of the specified account.
     *
     * @param code The currency code of the account to be validated.
     * @param uid The unique identifier of the user for whom the account ownership is being validated.
     * @throws Exception If the provided user is not the owner of the specified account.
     */

    public void validateAccountOwnership(String code, String uid) throws Exception {
        accountRepository.findByCodeAndOwnerUid(code, uid).orElseThrow();
    }

    /**
     * Validates the conversion process by ensuring that the sender's account has sufficient funds,
     * the currencies involved in the conversion are different, and the user is the owner of both accounts.
     *
     * @param convertDto The DTO containing the currencies and amount to be validated.
     * @param uid The unique identifier of the user initiating the conversion.
     * @throws Exception If any of the following conditions are not met:
     * - The sender's account does not have sufficient funds.
     * - The currencies involved in the conversion are the same.
     * - The user is not the owner of both sender and receiver accounts.
     */

    public void validateConversion(ConvertDto convertDto,String uid) throws Exception {
        ValidateDifferentCurrencyType(convertDto);
        ValidateAccountOwnerShip(convertDto,uid);
        ValidateAmount(convertDto.getAmount());
        ValidateSufficientFunds(accountRepository.findByCodeAndOwnerUid(convertDto.getFromCurrency(),uid).get(),convertDto.getAmount());
    }

    /**
     * Performs a currency conversion between two accounts owned by the same user.
     * The function validates the conversion process, calculates the converted amount,
     * updates the balances of the sender and receiver accounts, and creates transaction records.
     *
     * @param convertDto The DTO containing the currencies and amount to be converted.
     * @param user The user initiating the conversion.
     * @return The transaction record for the conversion from the sender's account.
     * @throws Exception If any of the following conditions are not met:
     * - The sender's account does not have sufficient funds.
     * - The currencies involved in the conversion are the same.
     * - The user is not the owner of both sender and receiver accounts.
     */



    public Transactions convertCurrency(ConvertDto convertDto,User user) throws Exception {

        validateConversion(convertDto,user.getUid());
        var rates = exchangeRatesService.getRates();
        var sendingRate = rates.get(convertDto.getFromCurrency());
        var receivingRate = rates.get(convertDto.getToCurrency());
        var computeAmount = (receivingRate/sendingRate) * convertDto.getAmount();
        Account fromAccount = accountRepository.findByCodeAndOwnerUid(convertDto.getFromCurrency(),user.getUid()).orElseThrow();
        Account toAccount = accountRepository.findByCodeAndOwnerUid(convertDto.getToCurrency(),user.getUid()).orElseThrow();
        fromAccount.setBalance(fromAccount.getBalance()-(convertDto.getAmount()*1.01));
        toAccount.setBalance(toAccount.getBalance()+computeAmount);
        accountRepository.saveAll(List.of(fromAccount,toAccount));

        var fromAccountTransaction = transactionService.createAccountTransactions(convertDto.getAmount(),Type.CONVERSION,convertDto.getAmount()*0.01,user,fromAccount);
        var toAccountNumber = transactionService.createAccountTransactions(computeAmount,Type.DEPOSIT,0.00,user,toAccount);

        return fromAccountTransaction;


    }





    /**
     * Checks if an account with the given currency code and user identifier exists.
     *
     * @param code The currency code of the account.
     * @param uid The unique identifier of the user.
     * @return True if the account exists, false otherwise.
     */
    public boolean existsByCodeAndOwnerUid(String code, String uid) {
        return accountRepository.existsByCodeAndOwnerUid(code, uid);
    }


    /**
     * Retrieves the account with the given currency code and user identifier.
     *
     * @param code The currency code of the account.
     * @param uid The unique identifier of the user.
     * @return The account if found, otherwise an empty Optional.
     */
    public Optional<Account> findByCodeAndOwnerUid(String code, String uid) {
        return accountRepository.findByCodeAndOwnerUid(code, uid);
    }

    /**
     * Saves the given account to the database.
     *
     * @param usdAccount The account to be saved.
     * @return The saved account.
     */
    public Account save(Account usdAccount) {
        return accountRepository.save(usdAccount);
    }
}
