package com.project.clothingaggregator.service;

import com.project.clothingaggregator.exception.NotFoundException;
import com.project.clothingaggregator.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LogServiceTest {

    @TempDir
    Path tempDir;

    @Mock
    private Map<String, TaskStatus> taskStatusMap = new ConcurrentHashMap<>();

    @Mock
    private Map<String, Path> taskResultMap;

    @InjectMocks
    private LogService logService;

    private Path logFile;

    @BeforeEach
    void setUp() {
        System.setProperty("test.environment", "true");

        logFile = tempDir.resolve("application.log");
        ReflectionTestUtils.setField(logService, "logFilePath", logFile.toString());
    }

    @Test
    void getLogByDate_ValidDate_ReturnsFilteredLog() throws IOException {
        Files.write(logFile, List.of(
                "2023-05-01 Line 1",
                "2023-05-01 Line 2",
                "2023-05-02 Line 3",
                "2023-05-01 Line 4"
        ));

        LocalDate filterDate = LocalDate.of(2023, 5, 1);

        Resource result = logService.getLogByDateSynchronized(filterDate);

        assertNotNull(result);
        assertTrue(result.exists());

        List<String> lines = Files.readAllLines(result.getFile().toPath());
        assertEquals(3, lines.size());
        assertTrue(lines.get(0).startsWith("2023-05-01"));
        assertTrue(lines.get(1).startsWith("2023-05-01"));
        assertTrue(lines.get(2).startsWith("2023-05-01"));
    }

    @Test
    void getLogByDate_NoMatchingLines_ReturnsEmptyFile() throws IOException {
        Files.write(logFile, List.of(
                "2023-05-02 Line 1",
                "2023-05-02 Line 2"
        ));

        LocalDate filterDate = LocalDate.of(2023, 5, 1);

        Resource result = logService.getLogByDateSynchronized(filterDate);

        assertNotNull(result);
        assertTrue(result.exists());

        List<String> lines = Files.readAllLines(result.getFile().toPath());
        assertTrue(lines.isEmpty());
    }

    @Test
    void getLogByDate_LogFileNotFound_ThrowsNotFoundException() {
        ReflectionTestUtils.setField(logService, "logFilePath", "/nonexistent/path.log");
        LocalDate filterDate = LocalDate.now();

        assertThrows(NotFoundException.class, () -> {
            logService.getLogByDateSynchronized(filterDate);
        });
    }

    @Test
    void startLogProcessing_ShouldReturnTaskIdAndSetPendingStatus() {
        LocalDate date = LocalDate.now();
        String taskId = logService.startLogProcessing(date);

        assertNotNull(taskId);
        assertDoesNotThrow(() -> UUID.fromString(taskId));
        assertEquals(TaskStatus.PENDING, logService.getTaskStatus(taskId));
    }

    @Test
    void getTaskStatus_ShouldReturnCorrectStatusLifecycle() {
        LocalDate date = LocalDate.now();
        String taskId = logService.startLogProcessing(date);

        assertEquals(TaskStatus.PENDING, logService.getTaskStatus(taskId));

        await().atMost(2, TimeUnit.SECONDS).until(() ->
                TaskStatus.PROCESSING == logService.getTaskStatus(taskId));
    }

    @Test
    void getLogFileById_ShouldReturnFileWhenTaskCompleted() throws IOException {
        Files.write(logFile, List.of(
                LocalDate.now() + " Test line 1",
                LocalDate.now() + " Test line 2"
        ));

        String taskId = logService.startLogProcessing(LocalDate.now());

        await().atMost(5, TimeUnit.SECONDS).until(() ->
                TaskStatus.COMPLETED == logService.getTaskStatus(taskId));

        Resource result = logService.getLogFileById(taskId);

        assertNotNull(result);
        assertTrue(result.exists());
    }

    @Test
    void getLogFileById_ShouldThrowWhenTaskNotCompleted() {
        String taskId = logService.startLogProcessing(LocalDate.now());

        assertThrows(IllegalStateException.class, () ->
                logService.getLogFileById(taskId));
    }

    @Test
    void getLogFileById_ShouldThrowWhenTaskFailed() {
        ReflectionTestUtils.setField(logService, "logFilePath", "/invalid/path.log");
        String taskId = logService.startLogProcessing(LocalDate.now());

        await().atMost(5, TimeUnit.SECONDS).until(() ->
                TaskStatus.FAILED == logService.getTaskStatus(taskId));

        assertThrows(IllegalStateException.class, () ->
                logService.getLogFileById(taskId));
    }

    @Test
    void processLogFile_ShouldHandleEmptyLogFile() throws IOException {
        Files.createFile(logFile);

        LocalDate date = LocalDate.now();
        Path result = logService.processLogFile(date);

        assertNotNull(result);
        assertEquals(0, Files.readAllLines(result).size());
    }

    @Test
    void processLogFile_ShouldCreateTempFileWithCorrectName() throws IOException {
        Files.write(logFile, List.of(LocalDate.now() + " Test line"));

        LocalDate date = LocalDate.now();
        Path result = logService.processLogFile(date);

        assertTrue(result.getFileName().toString().startsWith("logs-" + date));
        assertTrue(result.getFileName().toString().endsWith(".log"));
    }

    @Test
    public void testGetLogFileById_FilePathNull() throws NoSuchFieldException, IllegalAccessException {
        taskStatusMap = new ConcurrentHashMap<>();

        String taskId = "testTaskId";
        taskStatusMap.put(taskId, TaskStatus.COMPLETED);
        Field statusField = LogService.class.getDeclaredField("taskStatusMap");
        statusField.setAccessible(true);
        statusField.set(logService, taskStatusMap);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            logService.getLogFileById(taskId);
        });

        assertEquals("Log file not found for task ID: " + taskId, exception.getMessage());
    }

    @Test
    public void testGetLogFileById_FileNotExists() throws Exception {
        taskStatusMap = new ConcurrentHashMap<>();

        String taskId = "testTaskId";
        taskStatusMap.put(taskId, TaskStatus.COMPLETED);
        Field statusField = LogService.class.getDeclaredField("taskStatusMap");
        statusField.setAccessible(true);
        statusField.set(logService, taskStatusMap);

        taskResultMap = new ConcurrentHashMap<>();

        Path nonExistentFilePath = Path.of("non_existent_file.log");
        taskResultMap.put(taskId, nonExistentFilePath);

        Field resultField = LogService.class.getDeclaredField("taskResultMap");
        resultField.setAccessible(true);
        resultField.set(logService, taskResultMap);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            logService.getLogFileById(taskId);
        });

        assertEquals("Log file not found for task ID: " + taskId, exception.getMessage());
    }

    @Test
    public void testIsTestEnvironment_PropertyTrue() {
        System.setProperty("test.environment", "true");
        assertTrue(logService.isTestEnvironment());
        System.clearProperty("test.environment");
    }

    @Test
    public void testIsTestEnvironment_PropertyFalse() {
        System.setProperty("test.environment", "false");
        assertFalse(logService.isTestEnvironment());
        System.clearProperty("test.environment");
    }
}