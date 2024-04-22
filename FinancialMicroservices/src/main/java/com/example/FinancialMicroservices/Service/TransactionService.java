package com.example.FinancialMicroservices.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.FinancialMicroservices.Model.Account;
import com.example.FinancialMicroservices.Model.Transaction;
import com.example.FinancialMicroservices.Repository.AccountRepository;
import com.example.FinancialMicroservices.Repository.TransactionRepository;

@Service
public class TransactionService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FraudService fraudService;

    @Autowired
    private TransactionRepository transactionRepository;
      // Method to retrieve transaction history for an account
    public List<Transaction> getTransactionHistory(String accountNumber) {
        // Fetch transactions from the repository
        List<Transaction> transactions = transactionRepository.findByAccountNumber(accountNumber);
        return transactions;
    }

    // Method to deposit amount to an account
    public ResponseEntity<String> deposit(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            // Add the deposit amount to the existing balance
            account.setBalance(account.getBalance().add(amount));
            accountRepository.save(account);
            
            // Record the deposit transaction
            Transaction transaction = createTransaction(accountNumber, amount, "deposit");
            transactionRepository.save(transaction);
            
            return ResponseEntity.ok("Deposit successful");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }
    }

    // Method to withdraw amount from an account
    public ResponseEntity<String> withdraw(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            BigDecimal currentBalance = account.getBalance();
            if (currentBalance.compareTo(amount) >= 0) {
                // Subtract the withdrawal amount from the existing balance
                account.setBalance(currentBalance.subtract(amount));
                accountRepository.save(account);
                
                // Record the withdrawal transaction
                Transaction transaction = createTransaction(accountNumber, amount, "withdrawal");
                transactionRepository.save(transaction);
                
                return ResponseEntity.ok("Withdrawal successful");
            } else {
                // Return an error if the account has insufficient funds
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient funds");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }
    }

     // Helper method to create a transaction
    private Transaction createTransaction(String accountNumber, BigDecimal amount, String type) {
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(accountNumber);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setDate(LocalDateTime.now());
        transaction.setDescription(type.equals("deposit") ? "Deposited amount: " + amount : "Withdrew amount: " + amount);

        return transaction;
    }

    // Method to process a transaction and perform AML checks
    public ResponseEntity<String> processTransaction(String accountNumber, BigDecimal amount, String type) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            Transaction transaction = createTransaction(accountNumber, amount, type);

            boolean isAMLFlagged = fraudService.isAMLFlagged(transaction, account);

            if (isAMLFlagged) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Transaction flagged by AML check");
            } else {
                if (type.equals("deposit")) {
                    return deposit(accountNumber, amount);
                } else if (type.equals("withdrawal")) {
                    return withdraw(accountNumber, amount);
                }
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing transaction");
    }
}

