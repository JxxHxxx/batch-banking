package com.jxx.banking.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Transaction {

    private String accountNumber;
    private Date timestamp;
    private double amount;

}
