package br.com.zenon.representation.transaction;

import java.math.BigDecimal;

public record TransactionAccount(String name, BigDecimal oldBalance, BigDecimal newBalance) {}
