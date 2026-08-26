package br.com.zenon;

import br.com.zenon.representation.transaction.Transaction;
import br.com.zenon.representation.transaction.TransactionAccount;
import br.com.zenon.representation.transaction.TransactionType;

import java.math.BigDecimal;

public class TransactionMapper {

    public static Transaction fromCsvLine(String line){
        String[] splittedLine = line.split(",");
        return  new Transaction(
                Integer.parseInt(splittedLine[0]),
                TransactionType.valueOf(splittedLine[1]),
                new BigDecimal(splittedLine[2]),
                new TransactionAccount(splittedLine[3],new BigDecimal(splittedLine[4]),new BigDecimal(splittedLine[5])),
                new TransactionAccount(splittedLine[6],new BigDecimal(splittedLine[7]),new BigDecimal(splittedLine[8])),
                splittedLine[9].equals("1"),
                splittedLine[10].equals("1")
        );
    };
}
