package br.com.zenon;

import br.com.zenon.transaction.domain.Transaction;
import br.com.zenon.transaction.ingestor.TransactionIngestor;
import br.com.zenon.transaction.repository.TransactionRepository;
import br.com.zenon.transaction.repository.implementation.TransactionSQLRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class DBMain {
    void main(String[] args) throws IOException {
        String FILE_PATH = "data/PS_20174392719_1491204439457_log.csv";
        String FILE_BAD_PATH = "data/paysim_with_bad_data.csv";


        TransactionRepository transactionRepository = new TransactionSQLRepository();
        List<Transaction> transactionList = new TransactionIngestor(Path.of(FILE_PATH).toFile()).numberOfLines(10_000).start();
        this.saveTransactions(transactionList, transactionRepository);
        transactionRepository.findByOriginName("C1231006815").ifPresentOrElse(IO::println, () -> IO.println("Registro não encontrado"));
        transactionRepository.findByOriginName("C12345").ifPresentOrElse(IO::println, () -> IO.println("Registro não encontrado"));

    }

    private void saveTransactions(List<Transaction> transactions, TransactionRepository repository){
        long start = System.currentTimeMillis();
        transactions.forEach(repository::save);
        IO.println(String.format("Tempo em ms para inserção de %d registros %d",
                transactions.size(),
                (System.currentTimeMillis() - start)));
    }
}