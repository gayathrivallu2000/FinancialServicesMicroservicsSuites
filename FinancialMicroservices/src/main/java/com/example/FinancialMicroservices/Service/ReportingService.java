package com.example.FinancialMicroservices.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.FinancialMicroservices.Model.Transaction;
import com.example.FinancialMicroservices.Repository.TransactionRepository;

@Service
public class ReportingService {

    
    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> generateTransactionReport(String accountNumber, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59); // Set end time to end of the day
        return transactionRepository.findRecentTransactions(accountNumber, startDateTime, endDateTime);
    }
}
