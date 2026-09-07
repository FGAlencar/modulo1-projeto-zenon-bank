package br.com.zenon;

import br.com.zenon.transaction.TransactionReport;

import java.io.IOException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.ResourceBundle;

public class ZenonFraudDetectorApplication {
    void main(String[] args) throws IOException {
        String FILE_PATH = "data/PS_20174392719_1491204439457_log.csv";
        String FILE_BAD_PATH = "data/paysim_with_bad_data.csv";


        TransactionReport transactionReport = new TransactionReport(Path.of(FILE_PATH));
        TransactionReport.Report report =  transactionReport.generate();

        Locale locale = Locale.of(args.length > 0 ? args[0] :"pt");

        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        NumberFormat currencyFormatter = DecimalFormat.getCurrencyInstance(locale);
        currencyFormatter.setCurrency(Currency.getInstance("USD"));

        ResourceBundle reportBundle = ResourceBundle.getBundle("report", locale);

        IO.println(reportBundle.getString("label.total.transactions")+": "+ numberFormat.format(report.total()));
        IO.println(reportBundle.getString("label.total.frauds")+": " + numberFormat.format(report.totalFraud()));
        IO.println(reportBundle.getString("label.total.amount")+": " + currencyFormatter.format(report.totalAmount()));
    }
}