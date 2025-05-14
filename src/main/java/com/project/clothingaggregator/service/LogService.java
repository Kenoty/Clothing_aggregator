package com.project.clothingaggregator.service;

import com.project.clothingaggregator.exception.NotFoundException;
import com.project.clothingaggregator.model.TaskStatus;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;


@Service
public class LogService {

    @Value("${logging.file.name}")
    private String logFilePath;

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Map<String, TaskStatus> taskStatusMap = new ConcurrentHashMap<>();
    private final Map<String, Path> taskResultMap = new ConcurrentHashMap<>();

    public String startLogProcessing(LocalDate date) {
        String taskId = UUID.randomUUID().toString();
        taskStatusMap.put(taskId, TaskStatus.PENDING);

        executorService.submit(() -> {
            try {
                Thread.sleep(1000);
                taskStatusMap.put(taskId, TaskStatus.PROCESSING);
                System.out.println("[" + Thread.currentThread().getName()
                        + "] Status -> PROCESSING");

                int sleepTime = isTestEnvironment() ? 200 : 30000;

                Thread.sleep(sleepTime);
                System.out.println("[" + Thread.currentThread().getName()
                        + "] SleepTime -> " + sleepTime);
                Path resultFile = processLogFile(date);
                System.out.println("[" + Thread.currentThread().getName()
                        + "] SleepTime -> " + sleepTime + " ended");
                taskResultMap.put(taskId, resultFile);
                taskStatusMap.put(taskId, TaskStatus.COMPLETED);
                System.out.println("[" + Thread.currentThread().getName()
                        + "] Status -> COMPLETED");
            } catch (Exception e) {
                taskStatusMap.put(taskId, TaskStatus.FAILED);
            }
        });

        return taskId;
    }

    public TaskStatus getTaskStatus(String taskId) {
        return taskStatusMap.getOrDefault(taskId, null);
    }

    public Resource getLogFileById(String taskId) {
        TaskStatus status = taskStatusMap.get(taskId);
        if (status != TaskStatus.COMPLETED) {
            throw new IllegalStateException("Task not completed or doesn't exist");
        }

        Path filePath = taskResultMap.get(taskId);
        if (filePath == null || !Files.exists(filePath)) {
            throw new NotFoundException("Log file not found for task ID: " + taskId);
        }

        return new FileSystemResource(filePath.toFile());
    }

    public Path processLogFile(LocalDate date) throws IOException {
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

        return tempFile;
    }

    public Resource getLogByDateSynchronized(LocalDate date) throws IOException {
        return new FileSystemResource(processLogFile(date).toFile());
    }

    public boolean isTestEnvironment() {
        return "true".equals(System.getProperty("test.environment"));
    }
}
