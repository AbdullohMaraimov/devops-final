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

    public String update(Food food) {
        Food oldFood = get(food.getId());
        oldFood.setName(food.getName());
        oldFood.setOrigin(food.getOrigin());
        oldFood.setProducer(food.getProducer());
        oldFood.setPrice(food.getPrice());
        repo.save(oldFood);
        return "Food updated successfully";
    }
}
