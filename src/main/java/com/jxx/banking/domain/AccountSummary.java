package com.jxx.banking.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountSummary {
    private int id;
    private String accountNumber;
    private Double currentBalance;
}
