package br.com.zenon.transaction.domain;

import java.math.BigDecimal;

public record Transaction(int step,
                          TransactionType type,
                          BigDecimal amount,
                          TransactionAccount origin,
                          TransactionAccount recipient,
                          boolean isFraud,
                          boolean isFlaggedFraud) {}
