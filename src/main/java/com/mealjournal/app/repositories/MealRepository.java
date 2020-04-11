package com.mealjournal.app.repositories;

import com.mealjournal.app.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MealRepository extends JpaRepository<Meal, Long> {

}
