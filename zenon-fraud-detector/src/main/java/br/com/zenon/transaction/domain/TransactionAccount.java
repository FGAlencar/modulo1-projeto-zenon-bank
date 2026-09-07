package br.com.zenon.transaction.domain;

import java.math.BigDecimal;

public record TransactionAccount(String name, BigDecimal oldBalance, BigDecimal newBalance) {}
