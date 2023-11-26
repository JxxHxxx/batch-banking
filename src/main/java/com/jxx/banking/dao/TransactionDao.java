package com.jxx.banking.dao;

import com.jxx.banking.domain.Transaction;

import java.util.List;

public interface TransactionDao {

    List<Transaction> getTransactionByAccountNumber(String accountNumber);
}
