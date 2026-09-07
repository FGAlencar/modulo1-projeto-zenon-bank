package br.com.zenon;

import br.com.zenon.transaction.TransactionAnalyzer;
import br.com.zenon.transaction.TransactionIngestor;
import br.com.zenon.transaction.domain.Transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public class ZenonFraudDetectorApplication {
    void main() throws IOException {
        String FILE_PATH = "data/PS_20174392719_1491204439457_log.csv";
        String FILE_BAD_PATH = "data/paysim_with_bad_data.csv";

        Path path = Path.of(FILE_PATH);
        List<Transaction> transactions = new TransactionIngestor(path.toFile()).numberOfLines(50000).start();
        TransactionAnalyzer analyzer = new TransactionAnalyzer(transactions);

        IO.println(String.format("Total de fraudes: %d", analyzer.getFilteredTransactions(Transaction::isFraud).size()));
        IO.println(String.format("Top 3 fraudes de maior valor: %s", analyzer.getTopX(Transaction::isFraud, transaction -> transaction.amount().setScale(2, RoundingMode.HALF_EVEN), Comparator.comparing(Transaction::amount), 3L)));
        IO.println(String.format("Clientes Suspeitos: %s", analyzer.getTopX(Transaction::isFraud, transaction -> transaction.origin().name(), Comparator.comparing(Transaction::amount), 5L)));
        IO.println(String.format("Prejuízo Total: %s", analyzer.getReduced(Transaction::isFraud, Transaction::amount, BigDecimal.ZERO, BigDecimal::add)));
        IO.println(String.format("Fraudes por tipo: %s", analyzer.getGroupedQuantity(Transaction::isFraud, Transaction::type)));

    }
}