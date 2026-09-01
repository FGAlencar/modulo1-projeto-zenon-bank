package br.com.zenon;

import br.com.zenon.representation.transaction.Transaction;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ZenonFraudDetectorApplication {
    void main() throws IOException {
        Path path = Path.of("data/PS_20174392719_1491204439457_log.csv");
        //Path path = Path.of("data/paysim_with_bad_data.csv");
        long start = System.currentTimeMillis();
        PaySimTransactionIngestor ingestor = new PaySimTransactionIngestor(path.toFile());
        List<Transaction> transactionList = ingestor.numberOfLines(10).start();

        IO.println(System.currentTimeMillis() - start + " ms");

    }
}