package com.sample.ex11_food.repository;

import com.sample.ex11_food.model.FoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<FoodEntity, Long> {
}
