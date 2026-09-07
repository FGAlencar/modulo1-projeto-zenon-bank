package br.com.zenon;

import br.com.zenon.transaction.TransactionReport;

import java.io.IOException;
import java.nio.file.Path;

public class ZenonFraudDetectorApplication {
    void main() throws IOException {
        String FILE_PATH = "data/PS_20174392719_1491204439457_log.csv";
        String FILE_BAD_PATH = "data/paysim_with_bad_data.csv";

        Path path = Path.of(FILE_PATH);
        TransactionReport report = new TransactionReport(path);
        TransactionReport.Report statiscs =  report.read();

        IO.println("Total de linhas: " + statiscs.total());
        IO.println("Total de fraudes: " + statiscs.totalFraud());
        IO.println("Valor total transacionado: " + statiscs.totalAmount());
    }
}