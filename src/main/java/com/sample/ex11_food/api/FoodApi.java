package com.sample.ex11_food.api;

import com.sample.ex11_food.api.request.CreateAndEditFoodRequest;
import com.sample.ex11_food.api.response.FoodDetailView;
import com.sample.ex11_food.api.response.FoodView;
import com.sample.ex11_food.model.FoodEntity;
import com.sample.ex11_food.repository.FoodRepository;
import com.sample.ex11_food.service.FoodService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FoodApi {

    private final FoodService foodService;
    private final FoodRepository foodRepository;

    public FoodApi(FoodService foodService, FoodRepository foodRepository) {
        this.foodService = foodService;
        this.foodRepository = foodRepository;
    }

    @GetMapping("/foods")
    public List<FoodView> getFoods(){
        return foodService.getAllFoods();
    }

    @GetMapping("/food/{foodId}")
    public FoodDetailView viewFood(@PathVariable("foodId") Long foodId) {
        return foodService.getFoodDetail(foodId);
    }

    @PostMapping("/food")
    public FoodEntity postFood(@RequestBody CreateAndEditFoodRequest request) {
        return foodService.createFood(request);
//        return "postfood / name: " + request.getName() + ", address : " + request.getAddress();
    }

    @PutMapping("/food/{foodId}")
    public void editFood(@PathVariable("foodId") Long foodId, @RequestBody CreateAndEditFoodRequest request) {
        foodService.editFood(foodId, request);

//        return "editFood" + foodId + ", name: " + request.getName() + ", address : " + request.getAddress() ;
    }

    @DeleteMapping("/food/{foodId}")
    public void deleteFood(@PathVariable("foodId") Long foodId) {
        foodService.deleteFood(foodId);
    }

}
