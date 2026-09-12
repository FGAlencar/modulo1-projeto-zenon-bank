package br.com.zenon.transaction.repository.implementation;

import br.com.zenon.transaction.repository.TransactionRepository;
import br.com.zenon.transaction.domain.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransactionMapRepository implements TransactionRepository {
    private final Map<String, Transaction> transactionMap;

    public TransactionMapRepository(Map<String, Transaction> transactionMap) {
        Objects.requireNonNull(transactionMap);
        this.transactionMap = transactionMap;
    }

    public TransactionMapRepository(List<Transaction> transactionList) {
        Objects.requireNonNull(transactionList);
        this(transactionList.stream().collect(Collectors.toMap(t -> t.origin().name(), Function.identity())));
    }

    @Override
    public Optional<Transaction> findByOriginName(String name) {
        return Optional.ofNullable(this.transactionMap.get(name));
    }

    @Override
    public void save(Transaction transaction) {
        transactionMap.putIfAbsent(transaction.origin().name(), transaction);
    }
}
