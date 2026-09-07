package br.com.zenon.transaction.repository;

import br.com.zenon.transaction.domain.Transaction;

import java.util.Optional;

public interface TransactionRepository {
    Optional<Transaction> findByOriginName(String name);
}
