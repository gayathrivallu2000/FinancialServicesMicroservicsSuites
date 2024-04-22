package com.example.FinancialMicroservices.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.FinancialMicroservices.Model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountNumber(String accountNumber);
}

