package com.example.FinancialMicroservices.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.FinancialMicroservices.Model.Account;
import com.example.FinancialMicroservices.Model.Transaction;
import com.example.FinancialMicroservices.Service.FraudService;

@RestController
public class FraudController {

    private final FraudService fraudService;

    public FraudController(FraudService fraudService) {
        this.fraudService = fraudService;
    }

    @PostMapping("/checkFraud")
    public ResponseEntity<String> checkFraud(@RequestBody FraudCheckRequest request) {
        boolean isFraudulent = fraudService.isFraudulent(request.getTransaction(), request.getAccount());
        boolean isAMLFlagged = fraudService.isAMLFlagged(request.getTransaction(), request.getAccount());

        if (isFraudulent || isAMLFlagged) {
            return ResponseEntity.ok("Transaction is potentially fraudulent.");
        } else {
            return ResponseEntity.ok("Transaction is not fraudulent.");
        }
    }

    @PostMapping("/checkAML")
    public ResponseEntity<String> checkAML(@RequestBody FraudCheckRequest request) {
        boolean isAMLFlagged = fraudService.isAMLFlagged(request.getTransaction(), request.getAccount());

        if (isAMLFlagged) {
            return ResponseEntity.ok("Transaction flagged by AML check.");
        } else {
            return ResponseEntity.ok("Transaction passed AML check.");
        }
    }

    static class FraudCheckRequest {
        private Transaction transaction;
        private Account account;

        public Transaction getTransaction() {
            return transaction;
        }

        public void setTransaction(Transaction transaction) {
            this.transaction = transaction;
        }

        public Account getAccount() {
            return account;
        }

        public void setAccount(Account account) {
            this.account = account;
        }
    }
}
