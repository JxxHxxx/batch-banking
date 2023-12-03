package com.jxx.banking.reader;

import com.jxx.banking.dao.TransactionDao;
import com.jxx.banking.domain.AccountSummary;
import com.jxx.banking.domain.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TransactionApplierProcessor implements ItemProcessor<AccountSummary, AccountSummary> {

    private final TransactionDao transactionDao;

    @Override
    public AccountSummary process(AccountSummary summary) throws Exception {
        List<Transaction> transactions = transactionDao
                .getTransactionByAccountNumber(summary.getAccountNumber());

        for (Transaction transaction : transactions) {
            int idx = 0;
            log.info("txNo {} : {}", transaction, idx);
            idx++;
        }

        for (Transaction transaction : transactions) {
            summary.setCurrentBalance(summary.getCurrentBalance() + transaction.getAmount());
        }
        return summary;
    }
}
