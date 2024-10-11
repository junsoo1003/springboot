package com.sample.ex11_food.service;

import com.sample.ex11_food.api.request.CreateAndEditFoodRequest;
import com.sample.ex11_food.api.response.FoodDetailView;
import com.sample.ex11_food.api.response.FoodView;
import com.sample.ex11_food.model.FoodEntity;
import com.sample.ex11_food.model.MenuEntity;
import com.sample.ex11_food.repository.FoodRepository;
import com.sample.ex11_food.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class FoodService {
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private MenuRepository menuRepository;

    @Transactional
    public FoodEntity createFood(CreateAndEditFoodRequest request) {
        FoodEntity food = FoodEntity.builder()
                .name(request.getName())
                .address(request.getAddress())
                .createdAt(ZonedDateTime.now())
                .updateAt(ZonedDateTime.now())
                .build();
        foodRepository.save(food);

        request.getMenus().forEach((menu) -> {
            MenuEntity menuEntity = MenuEntity.builder()
                    .foodId(food.getId())
                    .name(menu.getName())
                    .price(menu.getPrice())
                    .createdAt(ZonedDateTime.now())
                    .updateAt(ZonedDateTime.now())
                    .build();
            menuRepository.save(menuEntity);
        });

        return food;
    }

    @Transactional
    public void editFood(Long foodId, CreateAndEditFoodRequest request) {
        FoodEntity food = foodRepository.findById(foodId).orElseThrow(() -> new RuntimeException("no food"));
        food.changeNameAndAddress(request.getName(), request.getAddress());
        foodRepository.save(food);

        List<MenuEntity> menus = menuRepository.findAllByFoodId(foodId);
        menuRepository.deleteAll(menus);

        request.getMenus().forEach((menu) -> {
            MenuEntity menuEntity = MenuEntity.builder()
                    .foodId(food.getId())
                    .name(menu.getName())
                    .price(menu.getPrice())
                    .createdAt(ZonedDateTime.now())
                    .updateAt(ZonedDateTime.now())
                    .build();
            menuRepository.save(menuEntity);
        });

    }

    public void deleteFood(Long foodId) {
        FoodEntity food = foodRepository.findById(foodId).orElseThrow();
        foodRepository.delete(food);

        List<MenuEntity> menus = menuRepository.findAllByFoodId(foodId);
        menuRepository.deleteAll(menus);
    }

    public List<FoodView> getAllFoods() {
        List<FoodEntity> foods = foodRepository.findAll();

        return foods.stream().map((food) ->
                FoodView.builder()
                        .id(food.getId())
                        .name(food.getName())
                        .address(food.getAddress())
                        .createdAt(food.getCreatedAt())
                        .updatedAt(food.getUpdateAt())
                        .build()
        ).toList();
    }

    public FoodDetailView getFoodDetail(Long foodId) {
        FoodEntity food = foodRepository.findById(foodId).orElseThrow();

        List<MenuEntity> menus = menuRepository.findAllByFoodId(foodId);
        return FoodDetailView.builder()
                .id(food.getId())
                .name(food.getName())
                .address(food.getAddress())
                .createdAt(food.getCreatedAt())
                .updatedAt(food.getUpdateAt())
                .menus(menus.stream().map((menu) -> FoodDetailView.Menu.builder()
                        .foodId(menu.getFoodId())
                        .name(menu.getName())
                        .price(menu.getPrice())
                        .createdAt(menu.getCreatedAt())
                        .updatedAt(menu.getUpdateAt())
                        .build()
                ).toList())
                .build();
    }
}
