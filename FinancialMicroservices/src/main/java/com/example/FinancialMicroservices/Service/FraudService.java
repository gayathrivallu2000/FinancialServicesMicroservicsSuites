package com.example.FinancialMicroservices.Service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.FinancialMicroservices.Model.Account;
import com.example.FinancialMicroservices.Model.Transaction;
import com.example.FinancialMicroservices.Repository.TransactionRepository;

@Service
public class FraudService {

    @Autowired
    private TransactionRepository transactionRepository;


    private static final BigDecimal FRAUD_THRESHOLD = new BigDecimal(1000);

    // Set of known fraudulent merchants
    private static final Set<String> FRAUDULENT_MERCHANTS = new HashSet<>();
    static {
        FRAUDULENT_MERCHANTS.add("FraudMerchant1");
        FRAUDULENT_MERCHANTS.add("FraudMerchant2");
    }

    public boolean isAMLFlagged(Transaction transaction, Account account) {
        // Check if the transaction amount exceeds a certain threshold
        BigDecimal transactionAmount = transaction.getAmount();
        if (transactionAmount.compareTo(new BigDecimal(1000)) >= 0) {
            // Flag the transaction if the amount is greater than or equal to 1000
            return true;
        }
    
        // Check if the transaction frequency is unusually high
        LocalDateTime transactionDate = transaction.getDate();
        List<Transaction> recentTransactions = transactionRepository.findRecentTransactions(account.getAccountNumber(), transactionDate.minusDays(30), transactionDate);
        int transactionCount = recentTransactions.size();
        if (transactionCount >= 100) {
            // Flag the transaction if there have been more than 5 transactions in the last 30 days
            return true;
        }
        return false;
    }

    public boolean isFraudulent(Transaction transaction, Account account) {
        // Check if the transaction amount exceeds the fraud threshold
        if (transaction.getAmount().compareTo(FRAUD_THRESHOLD) > 0) {
            // Transaction amount is greater than the threshold, consider it fraudulent
            return true;
        }

        
        if (FRAUDULENT_MERCHANTS.contains(transaction.getMerchant())) {
            // Merchant is known to be fraudulent, consider it fraudulent
            return true;
        }

        return false;
    }
}
