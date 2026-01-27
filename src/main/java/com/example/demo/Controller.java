package com.example.demo;

import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
@Data
public class Controller {

    private final FoodSerivce service;
    private final ApiMetrics apiMetrics;

    @GetMapping("/{foodId}")
    public Food get(@PathVariable Integer foodId) {
        final String api = "food_get";
        apiMetrics.incRequest(api);
        try {
            Food result = service.get(foodId);
            apiMetrics.incSuccess(api);
            return result;
        } catch (Exception e) {
            apiMetrics.incFailed(api, e.getClass().getSimpleName());
            throw e;
        }
    }

    @PutMapping
    public String put(@RequestBody Food food) {
        final String api = "food_put";
        apiMetrics.incRequest(api);
        try {
            String result = service.update(food);
            apiMetrics.incSuccess(api);
            return result;
        } catch (Exception e) {
            apiMetrics.incFailed(api, e.getClass().getSimpleName());
            throw e;
        }
    }

    @PostMapping
    public Food save(@RequestBody Food food) {
        final String api = "food_save";
        apiMetrics.incRequest(api);
        try {
            Food result = service.save(food);
            apiMetrics.incSuccess(api);
            return result;
        } catch (Exception e) {
            apiMetrics.incFailed(api, e.getClass().getSimpleName());
            throw e;
        }
    }

    @GetMapping
    public List<Food> findAll() {
        final String api = "food_find_all";
        apiMetrics.incRequest(api);
        try {
            List<Food> result = service.findAll();
            apiMetrics.incSuccess(api);
            return result;
        } catch (Exception e) {
            apiMetrics.incFailed(api, e.getClass().getSimpleName());
            throw e;
        }
    }
}
