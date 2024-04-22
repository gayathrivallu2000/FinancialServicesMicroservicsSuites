package com.example.FinancialMicroservices.Controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.FinancialMicroservices.Model.Transaction;
import com.example.FinancialMicroservices.Service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    // Endpoint to deposit an amount to an account
    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestParam String accountNumber, @RequestParam BigDecimal amount) {
        return transactionService.deposit(accountNumber, amount);
    }

    // Endpoint to withdraw an amount from an account
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestParam String accountNumber, @RequestParam BigDecimal amount) {
        return transactionService.withdraw(accountNumber, amount);
    }

    // Endpoint to retrieve transaction history for an account
    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable String accountNumber) {
        List<Transaction> transactions = transactionService.getTransactionHistory(accountNumber);
        if (transactions != null && !transactions.isEmpty()) {
            return ResponseEntity.ok(transactions);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to process a transaction with AML checks
    @PostMapping("/transactions/process")
    public ResponseEntity<String> processTransaction(
            @RequestParam String accountNumber,
            @RequestParam BigDecimal amount,
            @RequestParam String type) {
        return transactionService.processTransaction(accountNumber, amount, type);
    }

}
