package com.example.demo;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class FoodSerivce {

    private final Repo repo;

    public Food get(Integer foodId) {
        return repo.findById(foodId).get();
    }

    public Food save(Food food) {
        return repo.save(food);
    }

    public List<Food> findAll() {
        return (List<Food>) repo.findAll();
    }
}
