package com.example.demo;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class StudentService {

    private final Repo repo;

    public Student get(Integer studentId) {
        return repo.findById(studentId).get();
    }

    public Student save(Student student) {
        return repo.save(student);
    }

    public List<Student> findAll() {
        return (List<Student>) repo.findAll();
    }
}
