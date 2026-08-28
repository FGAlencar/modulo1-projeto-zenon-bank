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
import java.util.Arrays;
import java.util.List;

public class PaySimTransactionIngestor {
    private final File fileToIngest;
    private static final int DEFAULT_NUMBER_OF_LINES = 1000;
    private boolean readEntireFile = false;
    private int numberOfLines = DEFAULT_NUMBER_OF_LINES;

    public PaySimTransactionIngestor(File fileToIngest) {
        this.fileToIngest = fileToIngest;
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

    public List<Transaction> read() throws IOException {
        return this.readEntireFile
                ? this.readEntireFile(this.fileToIngest)
                : this.readFirstXLines(this.fileToIngest, this.numberOfLines);
    }

    private List<Transaction> readEntireFile(File file) throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        try(BufferedReader reader = this.readFile(file)){
            String header = reader.readLine();
            PaySimTransactionValidator validator = new PaySimTransactionValidator(header);

            String register;

            while ((register = reader.readLine()) != null){
                try{
                    String[] splittedRegister = register.split(",", -1);
                    validator.assertValidLine(splittedRegister);
                    transactions.add(PaySimTransactionMapper.fromCsvLine(splittedRegister));
                } catch (Exception e) {
                    System.err.printf("Error: %s | %s  -> %s%n", register, e.getClass().getName(),e.getMessage());
                }
            }

            return  transactions;
        }
    }

    private List<Transaction> readFirstXLines( File file, int numberOfLinesToRead) throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        try(BufferedReader reader = this.readFile(file)){
            String header = reader.readLine();
            PaySimTransactionValidator validator = new PaySimTransactionValidator(header);

            String register;
            int count = 0;
            while ((register = reader.readLine()) != null){
                if(count == numberOfLinesToRead){
                    break;
                }

                try{
                    String[] splittedRegister = register.split(",", -1);
                    validator.assertValidLine(splittedRegister);
                    transactions.add(PaySimTransactionMapper.fromCsvLine(splittedRegister));
                } catch (Exception e) {
                    System.err.printf("Error: %s | %s  -> %s%n", register, e.getClass().getName(),e.getMessage());
                }

                count++;

            }

            return transactions;
        }
    }

    private BufferedReader readFile(File file) throws FileNotFoundException {
        return new BufferedReader(new FileReader(file));
    }

    static class PaySimTransactionMapper {

        public static Transaction fromCsvLine(String[] splittedLine){
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

    static class PaySimTransactionValidator{
        private final String[] headers;

        public PaySimTransactionValidator(String headers){
            this.headers = headers.split(",", -1);
        };

        public void assertValidLine(String[] values){


            for(int i = 0; i < values.length; i++){
               isNotEmpty(this.headers[i], values[i]);
            }

            isPositive(headers[0], Integer.parseInt(values[0]));
            isValidEnum(headers[1], values[1], TransactionType.class);
            isGreaterOrEquals(headers[2], new BigDecimal(values[2]), new BigDecimal("0.00"));
            isGreaterOrEquals(headers[4], new BigDecimal(values[4]), new BigDecimal("0.00"));
            isGreaterOrEquals(headers[5], new BigDecimal(values[5]), new BigDecimal("0.00"));
            isGreaterOrEquals(headers[7], new BigDecimal(values[7]), new BigDecimal("0.00"));
            isGreaterOrEquals(headers[8], new BigDecimal(values[8]), new BigDecimal("0.00"));
            isIntegerIn(headers[9], Integer.parseInt(values[9]), List.of(0,1));
            isIntegerIn(headers[10], Integer.parseInt(values[10]), List.of(0,1));
        }


        private static <T extends Enum<T> >void isValidEnum(String field, String value, Class<T> clazz){
            try {
                Enum.valueOf(clazz, value);
            }catch (Exception e){
                throw new IllegalArgumentException(String.format("%s should be one of them %s : %s", field, Arrays.toString(clazz.getEnumConstants()), value));
            }
        }

        private  static void isNotEmpty(String field, String value){
            if(value.isBlank()){
                throw new IllegalArgumentException(String.format("%s should not be empty : %s", field, value));
            }
        }

        private static  void isPositive(String field, Number value){
            if(value.longValue() <= 0){
                throw new IllegalArgumentException(String.format("%s should be positive : %s", field, value));
            }
        }

      private static void isIntegerIn(String field, Integer value, List<Integer> list ){
            if(!list.contains(value)){
                throw new IllegalArgumentException(String.format("%s should be in %s: %s", field, list, value));
            }
      }

        private static void isGreaterOrEquals(String field, BigDecimal value, BigDecimal reference){
            if(value.compareTo(reference) <0 ){
                throw new IllegalArgumentException(String.format("%s should greater than or equals %s: %s", field, reference, value));
            }
        }
    }
}
