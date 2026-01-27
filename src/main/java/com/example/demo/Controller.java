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
        long start = System.currentTimeMillis();

        apiMetrics.incRequest(api);
        try {
            Food result = service.get(foodId);
            apiMetrics.incSuccess(api);
            return result;
        } catch (Exception e) {
            apiMetrics.incFailed(api, e.getClass().getSimpleName());
            throw e;
        } finally {
            apiMetrics.recordLatencyMs(api, System.currentTimeMillis() - start);
        }
    }

    @PostMapping
    public Food save(@RequestBody Food food) {
        final String api = "food_save";
        long start = System.currentTimeMillis();

        apiMetrics.incRequest(api);
        try {
            Food result = service.save(food);
            apiMetrics.incFoodAdded();
            apiMetrics.incSuccess(api);
            return result;
        } catch (Exception e) {
            apiMetrics.incFailed(api, e.getClass().getSimpleName());
            throw e;
        } finally {
            apiMetrics.recordLatencyMs(api, System.currentTimeMillis() - start);
        }
    }

    @GetMapping
    public List<Food> findAll() {
        final String api = "food_find_all";
        long start = System.currentTimeMillis();

        apiMetrics.incRequest(api);
        try {
            List<Food> result = service.findAll();
            apiMetrics.incSuccess(api);
            return result;
        } catch (Exception e) {
            apiMetrics.incFailed(api, e.getClass().getSimpleName());
            throw e;
        } finally {
            apiMetrics.recordLatencyMs(api, System.currentTimeMillis() - start);
        }
    }
}
