package com.project.clothingaggregator.service;

import com.project.clothingaggregator.exception.NotFoundException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    @Value("${logging.file.name}")
    private String logFilePath;

    public Resource getLogByDate(LocalDate date) throws IOException {
        Path logFile = Paths.get(logFilePath);

        if (!Files.exists(logFile)) {
            throw new NotFoundException("Cannot find main log file");
        }


        String datePrefix = date.toString();
        Path tempFile = Files.createTempFile("logs-" + datePrefix, ".log");

        try (Stream<String> lines = Files.lines(logFile);
             BufferedWriter writer = Files.newBufferedWriter(tempFile)) {

            lines.filter(line -> line.startsWith(datePrefix))
                    .forEach(line -> {
                        try {
                            writer.write(line);
                            writer.newLine();
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        }

        return new FileSystemResource(tempFile.toFile());
    }
}
