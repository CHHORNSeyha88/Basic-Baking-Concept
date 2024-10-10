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
import seyha.web.app.Bank_Concepts.service.TransactionService;
import seyha.web.app.Bank_Concepts.utils.RandomUtil;

import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Map;

@Component
@Getter
@RequiredArgsConstructor
public class AccountAsistant {

    private final TransactionsRepository transactionRepository;

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
            accountNum = new RandomUtil().generateRandomString(10);


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

     public void ValidateDifferentCurrencyType(ConvertDto convertDto) throws Exception {
        if(convertDto.getToCurrency().equals(convertDto.getFromCurrency())){
            throw new IllegalArgumentException("Conversion between the same currency type not allow");
        }
     }

     public void ValidateAccountOwnerShip(String id,String uid) throws Exception {
        accountRepository.findByCodeAndOwnerUid(id,uid).orElseThrow();
     }
}
