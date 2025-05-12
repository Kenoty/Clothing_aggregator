package com.project.clothingaggregator.service;

import com.project.clothingaggregator.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogServiceTest {

    @TempDir
    Path tempDir;

    @InjectMocks
    private LogService logService;

    @Test
    void getLogByDate_ValidDate_ReturnsFilteredLog() throws IOException {
        // Arrange
        Path logFile = tempDir.resolve("application.log");
        Files.write(logFile, List.of(
                "2023-05-01 Line 1",
                "2023-05-01 Line 2",
                "2023-05-02 Line 3",
                "2023-05-01 Line 4"
        ));

        ReflectionTestUtils.setField(logService, "logFilePath", logFile.toString());

        LocalDate filterDate = LocalDate.of(2023, 5, 1);

        // Act
        Resource result = logService.getLogByDate(filterDate);

        // Assert
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
        // Arrange
        Path logFile = tempDir.resolve("application.log");
        Files.write(logFile, List.of(
                "2023-05-02 Line 1",
                "2023-05-02 Line 2"
        ));

        ReflectionTestUtils.setField(logService, "logFilePath", logFile.toString());

        LocalDate filterDate = LocalDate.of(2023, 5, 1);

        // Act
        Resource result = logService.getLogByDate(filterDate);

        // Assert
        assertNotNull(result);
        assertTrue(result.exists());

        List<String> lines = Files.readAllLines(result.getFile().toPath());
        assertTrue(lines.isEmpty());
    }

    @Test
    void getLogByDate_LogFileNotFound_ThrowsNotFoundException() {
        // Arrange
        ReflectionTestUtils.setField(logService, "logFilePath", "/nonexistent/path.log");
        LocalDate filterDate = LocalDate.now();

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            logService.getLogByDate(filterDate);
        });
    }

//    @Test
//    void getLogByDate_IOException_ThrowsUncheckedIOException() throws IOException {
//        // Arrange
//        Path logFile = tempDir.resolve("application.log");
//        Files.write(logFile, List.of("2023-05-01 Test line"));
//
//        // Mock BufferedWriter to throw IOException
//        try (var mockedFiles = mockStatic(Files.class)) {
//            mockedFiles.when(() -> Files.lines(logFile)).thenReturn(Files.lines(logFile));
//            mockedFiles.when(() -> Files.newBufferedWriter(any(Path.class)))
//                    .thenThrow(new IOException("Test error"));
//
//            ReflectionTestUtils.setField(logService, "logFilePath", logFile.toString());
//            LocalDate filterDate = LocalDate.of(2023, 5, 1);
//
//            // Act & Assert
//            assertThrows(UncheckedIOException.class, () -> {
//                logService.getLogByDate(filterDate);
//            });
//        }
//    }

//    @Test
//    void getLogByDate_ValidatesTempFileDeletedAfterUse() throws IOException {
//        // Arrange
//        Path logFile = tempDir.resolve("application.log");
//        Files.write(logFile, List.of("2023-05-01 Test line"));
//        ReflectionTestUtils.setField(logService, "logFilePath", logFile.toString());
//
//        // Act
//        Resource result = logService.getLogByDate(LocalDate.of(2023, 5, 1));
//
//        // Assert - verify temp file is deleted when resource is closed
//        Path tempFilePath = result.getFile().toPath();
//        assertTrue(Files.exists(tempFilePath));
//
//        result.getInputStream().close(); // Simulate resource usage completion
//        assertFalse(Files.exists(tempFilePath)); // File should be deleted
//    }
}