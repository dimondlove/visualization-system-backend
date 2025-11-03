package com.rudnev.visualizationsystembackend.controller;

import com.rudnev.visualizationsystembackend.model.ReactorPoint;
import com.rudnev.visualizationsystembackend.model.ReactorRequest;
import com.rudnev.visualizationsystembackend.service.ReactorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reactor")
@CrossOrigin(origins = "*")
public class ReactorController {
    private final ReactorService service;

    public ReactorController(ReactorService service) {
        this.service = service;
    }

    @PostMapping("/calculateAll")
    public ResponseEntity<List<Map<String, Object>>> calculateAll(@RequestBody ReactorRequest request) {
        List<ReactorPoint> data = service.calculateAll(request);

        List<Map<String, Object>> ordered = new ArrayList<>();
        for (ReactorPoint p : data) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("L", p.getL());
            m.put("Xa", p.getXa());
            m.put("Xb", p.getXb());
            m.put("Xc", p.getXc());
            m.put("Xd", p.getXd());
            m.put("sum", p.getSum());
            ordered.add(m);
        }

        return ResponseEntity.ok(ordered);
    }

    @PostMapping("/calculateAtL")
    public ResponseEntity<Map<String, Object>> calculateAtL(@RequestBody ReactorRequest request) {
        ReactorPoint p = service.calculateAtL(request);
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("L", p.getL());
        m.put("Xa", p.getXa());
        m.put("Xb", p.getXb());
        m.put("Xc", p.getXc());
        m.put("Xd", p.getXd());
        m.put("sum", p.getSum());
        return ResponseEntity.ok(m);
    }
}
