package com.project.clothingaggregator.controller;

import com.project.clothingaggregator.model.TaskStatus;
import com.project.clothingaggregator.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @GetMapping("/by-date")
    @Operation(
            summary = "Download logs by date",
            description = "Downloads log file for specified date in plain text format")
    public ResponseEntity<Resource> getLogsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) throws IOException {

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"logs-" + date + ".log\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(logService.getLogByDateSynchronized(date));
    }

    @GetMapping("/ask/by-date")
    @Operation(
            summary = "Start log processing for a specific date",
            description = "Initiates asynchronous processing of logs for the given date. "
                    + "Returns a unique task ID to track progress.")
    public ResponseEntity<String> getIdForLogs(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {
        return ResponseEntity.ok(logService.startLogProcessing(date));
    }

    @GetMapping("/check")
    @Operation(
            summary = "Check processing status of a log task",
            description = "Returns the current status of an asynchronous log processing task by its ID")
    public ResponseEntity<TaskStatus> checkLogsStatus(
            @RequestParam String taskId) {
        return ResponseEntity.ok(logService.getTaskStatus(taskId));
    }

    @GetMapping("/download")
    @Operation(
            summary = "Download processed log file",
            description = "Downloads the processed log file for a completed task. "
                    + "The file contains logs filtered by the original date criteria.")
    public ResponseEntity<Resource> downloadLogsByTaskId(
            @RequestParam String taskId) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"logs-for-id" + taskId + ".log\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(logService.getLogFileById(taskId));
    }
}
