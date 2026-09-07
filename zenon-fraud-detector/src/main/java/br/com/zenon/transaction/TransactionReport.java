package br.com.zenon.transaction;

import br.com.zenon.transaction.domain.Transaction;
import br.com.zenon.transaction.ingestor.PaySimTransactionMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class TransactionReport {
    private final Path path;

    public TransactionReport(Path path) {
        this.path = path;
    }

    public Report generate() throws IOException {
        try(Stream<String> inputStream =  Files.lines(path)){
            return inputStream.skip(1)
                .map(line -> line.split(",", -1))
                .map(PaySimTransactionMapper::fromCsvLine)
                .reduce(Report.DEFAULT, Report::addInformation, Report::combine);

        }
    }

    public record Report(Long total, Long totalFraud, BigDecimal totalAmount){
        public static final Report DEFAULT = new Report(0L,0L, BigDecimal.ZERO);

        private Report addInformation(Transaction transaction){
            return new Report(total + 1,
                    totalFraud + (transaction.isFraud() ? 1 :0),
                    totalAmount.add(transaction.amount()));
        }

        private Report combine(Report report){
            return new Report(total + report.total,
                    totalFraud + report.total(),
                    totalAmount.add(report.totalAmount()));
        }

    };

}
