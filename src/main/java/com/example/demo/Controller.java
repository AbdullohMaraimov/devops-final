package com.example.demo;

import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
@Data
public class Controller {

    private final StudentService service;

    @GetMapping("/{studentId}")
    public Student get(@PathVariable Integer studentId) {
        return service.get(studentId);
    }

    @PostMapping
    public Student save(@RequestBody Student student) {
        return service.save(student);
    }

    @GetMapping
    public List<Student> findAll() {
        return service.findAll();
    }

}
