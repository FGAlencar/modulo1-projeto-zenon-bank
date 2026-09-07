package br.com.zenon.transaction.ingestor;

import br.com.zenon.transaction.domain.Transaction;
import br.com.zenon.transaction.domain.TransactionAccount;
import br.com.zenon.transaction.domain.TransactionType;

import java.math.BigDecimal;

public class PaySimTransactionMapper {

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
