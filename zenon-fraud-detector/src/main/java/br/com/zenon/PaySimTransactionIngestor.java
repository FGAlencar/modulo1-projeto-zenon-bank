package br.com.zenon;

import br.com.zenon.representation.transaction.Transaction;
import br.com.zenon.representation.transaction.TransactionAccount;
import br.com.zenon.representation.transaction.TransactionType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PaySimTransactionIngestor {

    private final File fileToIngest;
    private static final int DEFAULT_NUMBER_OF_LINES = 1000;
    private boolean readEntireFile = false;
    private int numberOfLines = DEFAULT_NUMBER_OF_LINES;

    public PaySimTransactionIngestor(File fileToIngest) {
        this.fileToIngest = fileToIngest;
    }

    public List<Transaction> read() throws IOException {
        return this.readEntireFile
                ? this.readEntireFile(this.fileToIngest)
                : this.readFirstXLines(this.fileToIngest, this.numberOfLines);
    }

    private List<Transaction> readEntireFile(File file) throws IOException {
        String line;
        List<Transaction> transactions = new ArrayList<>();

        BufferedReader reader = this.readFile(file);
        reader.readLine(); //ignoring header line
        while ((line = reader.readLine()) != null){
            transactions.add(PaySimTransactionMapper.fromCsvLine(line));
        }
        reader.close();
        return  transactions;
    }

    private List<Transaction> readFirstXLines( File file, int numberOfLinesToRead) throws IOException {
        String line;
        List<Transaction> transactions = new ArrayList<>();

        BufferedReader reader = this.readFile(file);
        reader.readLine(); //ignoring header line;
        int count = 0;
        while ((line = reader.readLine()) != null){
            if(count == numberOfLinesToRead){
                break;
            }
            transactions.add(PaySimTransactionMapper.fromCsvLine(line));
            count++;
        }
        reader.close();
        return transactions;
    }

    private BufferedReader readFile(File file) throws FileNotFoundException {
        return new BufferedReader(new FileReader(file));
    }

    public PaySimTransactionIngestor entireFile() {
        this.readEntireFile = true;
        this.numberOfLines = DEFAULT_NUMBER_OF_LINES;
        return this;
    }

    public PaySimTransactionIngestor numberOfLines(int numberOfLines) {
        this.numberOfLines = numberOfLines;
        this.readEntireFile = false;
        return this;
    }

    static class PaySimTransactionMapper {

        public static Transaction fromCsvLine(String line){
            String[] splittedLine = line.split(",");
            return  new Transaction(
                    Integer.parseInt(splittedLine[0]),
                    TransactionType.valueOf(splittedLine[1]),
                    new BigDecimal(splittedLine[2]),
                    new TransactionAccount(splittedLine[3],new BigDecimal(splittedLine[4]),new BigDecimal(splittedLine[5])),
                    new TransactionAccount(splittedLine[6],new BigDecimal(splittedLine[7]),new BigDecimal(splittedLine[8])),
                    "1".equals(splittedLine[9]),
                    "1".equals(splittedLine[10])
            );
        };
    }
}
