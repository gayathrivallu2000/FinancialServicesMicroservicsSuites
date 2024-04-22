package com.example.FinancialMicroservices.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.FinancialMicroservices.Model.Account;
import com.example.FinancialMicroservices.Model.Transaction;
import com.example.FinancialMicroservices.Repository.AccountRepository;
import com.example.FinancialMicroservices.Repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public void updateAccountBalance(String accountNumber, BigDecimal amount, boolean isDeposit) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            BigDecimal currentBalance = account.getBalance();
            if (isDeposit) {
                account.setBalance(currentBalance.add(amount));
                saveTransaction(accountNumber, amount, "deposit");
            } else {
                account.setBalance(currentBalance.subtract(amount));
                saveTransaction(accountNumber, amount, "withdrawal");
            }
            accountRepository.save(account);
        }
    }

    private void saveTransaction(String accountNumber, BigDecimal amount, String type) {
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(accountNumber);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setDate(LocalDateTime.now());
        transaction.setDescription("Transaction of type: " + type);
        
        transactionRepository.save(transaction);
    }

    public Account findAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public Optional<Account> updateAccount(Long id, Account accountDetails) {
        Optional<Account> optionalAccount = accountRepository.findById(id);

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            
            if (accountDetails != null) {
                account.setAccountNumber(accountDetails.getAccountNumber());
                account.setBalance(accountDetails.getBalance());
                account.setOwner(accountDetails.getOwner());
                
                accountRepository.save(account);
            }
            return Optional.of(account);
        } else {
            return Optional.empty();
        }
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
}
