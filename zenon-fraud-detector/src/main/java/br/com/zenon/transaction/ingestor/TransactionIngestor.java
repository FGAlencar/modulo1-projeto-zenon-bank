package br.com.zenon.transaction.ingestor;

import br.com.zenon.transaction.domain.Transaction;
import br.com.zenon.transaction.domain.TransactionAccount;
import br.com.zenon.transaction.domain.TransactionType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TransactionIngestor {
    private final File fileToIngest;
    private static final int DEFAULT_NUMBER_OF_LINES = Integer.MAX_VALUE;
    private int numberOfLines = DEFAULT_NUMBER_OF_LINES;

    public TransactionIngestor(File fileToIngest) {
        this.fileToIngest = fileToIngest;
    }

    public TransactionIngestor numberOfLines(int numberOfLines) {
        this.numberOfLines = numberOfLines;
        return this;
    }

    public TransactionIngestor entireFile() {
        this.numberOfLines = DEFAULT_NUMBER_OF_LINES;
        return this;
    }

    public List<Transaction> start() throws IOException {
       return this.readFile();
    }

    private List<Transaction> readFile() throws IOException{
        List<Transaction> transactions = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(this.fileToIngest))){
            String header = reader.readLine(); //primeira linha sempre é o header
            PaySimTransactionValidator validator = new PaySimTransactionValidator(header);
            String register;
            int count = 0;
            while ( (register = reader.readLine()) != null && count < this.numberOfLines){
                this.mapTransaction(validator, register).ifPresent(transactions::add);
                count++;
            }
            return transactions;
        }
    }

    private Optional<Transaction> mapTransaction(PaySimTransactionValidator validator, String register){
        try{
            String[] splittedRegister = register.split(",", -1);
            validator.assertValidLine(splittedRegister);
            return Optional.of(PaySimTransactionMapper.fromCsvLine(splittedRegister));
        } catch (Exception e) {
            System.err.printf("Error: %s | %s  -> %s%n", register, e.getClass().getName(),e.getMessage());
            return Optional.empty();
        }
    }
}
