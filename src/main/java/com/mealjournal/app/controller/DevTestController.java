package com.mealjournal.app.controller;

import com.mealjournal.app.model.Meal;
import com.mealjournal.app.model.User;
import com.mealjournal.app.repositories.MealRepository;
import com.mealjournal.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/dev-test")
public class DevTestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MealRepository mealRepository;

    @GetMapping("/")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/meals")
    public List<Meal> getAllMeals() {
        return mealRepository.findAll();
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable(name = "id") Long id) {
        return userRepository.findById(id).orElseThrow(null);
    }

    @GetMapping("/populate")
    public String populateDB() {
        userRepository.deleteAll();
        mealRepository.deleteAll();

        User shashank = new User();
        shashank.setName("Shashank Raj");
        shashank.setEmail("shashank@meals.com");
        shashank.setSaltedHashedPass("shawshank");
        userRepository.save(shashank);

        User sushant = new User();
        sushant.setName("Sushant Sharma");
        sushant.setEmail("sushant@meals.com");
        sushant.setSaltedHashedPass("chhote");
        userRepository.save(sushant);

        Meal breakfast = new Meal.Builder()
                .user(shashank)
                .mealName("Bread")
                .mealDateTime(new Date())
                .calories(200)
                .build();
        mealRepository.save(breakfast);
        Meal dinner = new Meal.Builder()
                .user(sushant)
                .mealName("Pizza")
                .mealDateTime(new Date())
                .calories(600)
                .build();
        mealRepository.save(dinner);

        System.out.println(breakfast.getUser().getId());
        System.out.println(dinner.getUser().getId());
        shashank.addMeal(breakfast);
        userRepository.save(shashank);
        sushant.addMeal(dinner);
        userRepository.save(sushant);

        return "populated";
    }

}
