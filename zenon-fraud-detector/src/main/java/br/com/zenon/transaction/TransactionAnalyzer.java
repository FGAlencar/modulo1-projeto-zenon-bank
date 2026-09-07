package br.com.zenon.transaction;

import br.com.zenon.transaction.domain.Transaction;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionAnalyzer {
    private final List<Transaction> transactions;

    public TransactionAnalyzer(List<Transaction> transactions){
        this.transactions = transactions;
    }

   public List<Transaction> getAllTransaction(){
        return this.transactions;
   }

   public List<Transaction> getFilteredTransactions(Predicate<Transaction> predicate){
        return this.getAllTransaction().stream().filter(predicate).toList();
   }

    public <R> Set<R> getTopX(Predicate<Transaction> predicate,
                              Function<? super Transaction, R> mapper,
                              Comparator<? super Transaction> comparator,
                              Long limit){
        Stream<R> stream = this.getAllTransaction().stream().filter(predicate).sorted(comparator.reversed()).map(mapper).distinct();

        if(limit != null){
            stream = stream.limit(limit);
        }

        return stream.collect(Collectors.toSet());
    }

    public <R> R getReduced(Predicate<Transaction> predicate, Function<? super Transaction, R> mapper, R indentifier, BinaryOperator<R> accumulator){
        return this.getAllTransaction()
                .stream()
                .filter(predicate)
                .map(mapper)
                .reduce(indentifier, accumulator);
    }

    public <K> Map<K, Long> getGroupedQuantity(Predicate<Transaction> predicate, Function<? super Transaction, K> groupFunction  ){
        return this.getAllTransaction()
                .stream()
                .filter(predicate)
                .collect(Collectors.groupingBy(groupFunction, Collectors.counting()));
    }

}
