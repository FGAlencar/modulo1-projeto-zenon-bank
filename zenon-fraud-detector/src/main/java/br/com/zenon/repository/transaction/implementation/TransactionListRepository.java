package br.com.zenon.repository.transaction.implementation;

import br.com.zenon.repository.transaction.TransactionRepository;
import br.com.zenon.representation.transaction.Transaction;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TransactionListRepository implements TransactionRepository {
    private final List<Transaction> transactions;

     public  TransactionListRepository(List<Transaction> transactions){
         Objects.requireNonNull(transactions);
         this.transactions = transactions;
     }


    @Override
    public Optional<Transaction> findByOriginName(String name) {
        return this.transactions.stream()
                .filter(transaction -> transaction.origin().name().equals(name))
                .findFirst();
    }
}
