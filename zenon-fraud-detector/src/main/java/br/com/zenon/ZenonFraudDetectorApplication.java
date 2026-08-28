package br.com.zenon;

import java.io.IOException;
import java.nio.file.Path;

public class ZenonFraudDetectorApplication {
    void main() throws IOException {
        //Path path = Path.of("data/PS_20174392719_1491204439457_log.csv");
        Path path = Path.of("data/paysim_with_bad_data.csv");
        long start = System.currentTimeMillis();
        new PaySimTransactionIngestor(path.toFile())
                .entireFile()
                .read()
                .forEach(IO::println);

        IO.println(System.currentTimeMillis() - start + " ms");

    }
}