package br.com.zenon.representation.transaction;

import java.math.BigDecimal;

public record Transaction(int step,
                          TransactionType type,
                          BigDecimal amount,
                          TransactionAccount origin,
                          TransactionAccount recipient,
                          boolean isFraud,
                          boolean isFlaggedFraud) {}
