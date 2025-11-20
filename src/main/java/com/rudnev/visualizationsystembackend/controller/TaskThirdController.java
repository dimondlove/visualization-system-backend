package com.rudnev.visualizationsystembackend.controller;

import com.rudnev.visualizationsystembackend.model.TaskThirdResponse;
import com.rudnev.visualizationsystembackend.service.TaskThirdService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/task3")
@CrossOrigin(origins = "*")
public class TaskThirdController {
    private final TaskThirdService service;

    public TaskThirdController(TaskThirdService service) {
        this.service = service;
    }

    @PostMapping("/calc")
    public ResponseEntity<?> calculate(
            @RequestParam("file") MultipartFile file,
            @RequestParam("d") int d,
            @RequestParam("Ne") int Ne,
            @RequestParam("Nu") int Nu,
            @RequestParam("Ftable") double Ftable,
            @RequestParam("method") int method // 2 or 3
    ) {
        try {
            TaskThirdResponse resp = service.process(file, d, Ne, Nu, Ftable, method);
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Internal error: " + ex.getMessage());
        }
    }
}
