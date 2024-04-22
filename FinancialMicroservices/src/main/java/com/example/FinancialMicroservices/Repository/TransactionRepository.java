package com.example.FinancialMicroservices.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.FinancialMicroservices.Model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountNumber(String accountNumber);

    @Query("SELECT t FROM Transaction t WHERE t.accountNumber = ?1 AND t.date BETWEEN ?2 AND ?3")
    List<Transaction> findRecentTransactions(String accountNumber, LocalDateTime startDate, LocalDateTime endDate);

    
}
