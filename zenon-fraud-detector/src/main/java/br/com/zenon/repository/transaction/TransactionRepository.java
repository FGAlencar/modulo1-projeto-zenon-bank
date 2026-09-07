package br.com.zenon.repository.transaction;

import br.com.zenon.representation.transaction.Transaction;

import java.util.Optional;

public interface TransactionRepository {
    Optional<Transaction> findByOriginName(String name);
}
