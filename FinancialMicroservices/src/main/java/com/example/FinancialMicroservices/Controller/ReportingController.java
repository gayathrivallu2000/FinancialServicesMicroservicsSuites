package com.example.FinancialMicroservices.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.FinancialMicroservices.Model.Transaction;
import com.example.FinancialMicroservices.Service.ReportingService;

@RestController
@RequestMapping("/reports")
public class ReportingController {

    @Autowired
    private ReportingService reportingService;

    // Endpoint to generate a transaction report for an account within a date range
    @GetMapping("/transactions/{accountNumber}")
    public ResponseEntity<List<Transaction>> generateTransactionReport(
            @PathVariable String accountNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<Transaction> report = reportingService.generateTransactionReport(accountNumber, startDate, endDate);
        
        if (report != null && !report.isEmpty()) {
            return ResponseEntity.ok(report);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Other endpoints for generating different types of reports can be added here

}
