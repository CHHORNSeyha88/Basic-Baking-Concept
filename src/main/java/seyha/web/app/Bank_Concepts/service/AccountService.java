package seyha.web.app.Bank_Concepts.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seyha.web.app.Bank_Concepts.dto.AccountDto;
import seyha.web.app.Bank_Concepts.dto.TransferDto;
import seyha.web.app.Bank_Concepts.entity.Account;
import seyha.web.app.Bank_Concepts.entity.Transactions;
import seyha.web.app.Bank_Concepts.entity.User;
import seyha.web.app.Bank_Concepts.repository.AccountRepository;
import seyha.web.app.Bank_Concepts.service.assist.AccountAsistant;

import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountAsistant accountAsistant;
    private final ExchangeRatesService exchangeService;

    public Account createAccount(AccountDto accountDto, User user) throws Exception {
        return accountAsistant.createAccount(accountDto, user);
    }

    public List<Account> getUserAccounts(String uid) {
        return accountRepository.findAllByOwnerUid(uid);
    }

    public Transactions transferFunds(TransferDto transferDto, User user) throws OperationNotSupportedException {

        var senderAccount = accountRepository.findByCodeAndOwnerUid(transferDto.getCode(), user.getUid()).orElseThrow(
                () -> new UnsupportedOperationException("Sender account not found!")
        );
        var receiverAccount = accountRepository.findByAccountNumber(transferDto.getRecipientAccountNumber()).orElseThrow(
                () -> new UnsupportedOperationException("Receiver account not found!")
        );

        return accountAsistant.transferPerformance(senderAccount, receiverAccount, transferDto.getAmount(), user);
    }
    /**
     * Retrieves the current exchange rates for all supported currencies.
     *
     * @return A map of currency codes to their exchange rates.
     */
    public Map<String, Double> getExchangeRate() {
        return exchangeService.getRates();
    }
}
