package com.example.FinancialMicroservices.Model;

public class FraudCheckRequest {
    private Transaction transaction;
    private Account account;

    // Getters and setters
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
