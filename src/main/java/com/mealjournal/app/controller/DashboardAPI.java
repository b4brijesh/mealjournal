package com.mealjournal.app.controller;

import com.mealjournal.app.exceptions.JournalAppException;
import com.mealjournal.app.model.Meal;
import com.mealjournal.app.model.User;
import com.mealjournal.app.repositories.MealRepository;
import com.mealjournal.app.repositories.UserRepository;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class DashboardAPI {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MealRepository mealRepository;

    private static JSONObject success;

    static {
        success = new JSONObject();
        success.put("status", "success");
    }

    @ExceptionHandler(JournalAppException.class)
    public JSONObject handleCustomException(Exception exception) {
        JSONObject error = new JSONObject();
        error.put("status", "error");
        error.put("errorText", exception.getMessage());
        return error;
    }

    public User getCurrentUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName()).orElseThrow(null);
        // todo: add proper exception
    }

    @GetMapping("/profile")
    public JSONObject userData(Authentication authentication) {
        return userData(getCurrentUser(authentication));
    }

    public JSONObject userData(User user) {
        JSONObject data = new JSONObject();
        data.put("fullname", user.getName());
        data.put("dob", user.getDob());
        data.put("height", user.getHeight());
        data.put("weight", user.getWeight());
        data.put("goal", user.getDailyCalorieGoal());
        return data;
    }

    @GetMapping("/update-profile")
    public JSONObject updateProfile(Authentication authentication,
                                 @RequestParam(name = "fullname", required = true) String name,
                                 @RequestParam(name = "dob", required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dob,
                                 @RequestParam(name = "height", required = false) String height,
                                 @RequestParam(name = "weight", required = false) String weight,
                                    @RequestParam(name = "goal", required = false) String goal) {
        User user = getCurrentUser(authentication);
        user.setName(name);
        user.setDob(dob);
        Integer h = user.getHeight(),w = user.getWeight(), g = user.getDailyCalorieGoal();
        // todo: reflow this
        if (!height.equals("null")) h = Integer.parseInt(height);
        if (!weight.equals("null")) w = Integer.parseInt(weight);
        if (!goal.equals("null")) g = Integer.parseInt(goal);

        user.setHeight(h);
        user.setWeight(w);
        user.setDailyCalorieGoal(g);
        userRepository.save(user);
        return success;
    }

    @GetMapping("/addMeal")
    public JSONObject addMeal(Authentication authentication,
                              @RequestParam("newMealName") String name,
                              @RequestParam("newMealDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
                              @RequestParam("newMealCalories") Integer calories) {

        // todo: validate parameters

        // once validated delegate to addMeal
        addMeal(getCurrentUser(authentication), name, date, calories);
        return success;
        // todo: add a message regarding adding of meal
    }

    public void addMeal(User user, String name, Date date, Integer calories) {
        Meal meal = new Meal.Builder()
                .user(user)
                .mealName(name)
                .mealDateTime(date)
                .calories(calories)
                .build();
        mealRepository.save(meal);
        // todo: check if user already has same meal id
        user.addMeal(meal);
        userRepository.save(user);

        System.out.println("Added meal " + name + " to " + user.getName());
    }

    @GetMapping("/removeMeal")
    public JSONObject removeMeal(Authentication authentication,
                              @RequestParam("mealId") String mealId) {

        // todo: validate parameters

        // once validated delegate to addMeal
        Long Id = Long.parseLong(mealId);
        removeMeal(getCurrentUser(authentication), Id);
        return success;
        // todo: add a message regarding adding of meal
    }

    public void removeMeal(User user, Long mealId) {
        Optional<Meal> meal = mealRepository.findById(mealId);
        if (meal.isPresent() && meal.get().getUser().equals(user)) {
            user.removeMeal(meal.get());
            mealRepository.deleteById(mealId);
            userRepository.save(user);
        }
    }

    @GetMapping("/editMeal")
    public JSONObject editMeal(Authentication authentication,
                                 @RequestParam("mealId") String mealId,
                               @RequestParam("mealName") String mealName,
                               @RequestParam("mealDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date mealDate,
                               @RequestParam("mealCalories") String mealCalories) {

        // todo: validate parameters

        // once validated delegate to addMeal
        Long Id = Long.parseLong(mealId);
        Integer calories = Integer.parseInt(mealCalories);
        editMeal(getCurrentUser(authentication), Id, mealName, mealDate, calories);
        return success;
        // todo: add a message regarding adding of meal
    }

    public void editMeal(User user, Long mealId, String mealName, Date mealDate, Integer mealCalories) {
        Optional<Meal> meal = mealRepository.findById(mealId);
        if (meal.isPresent() && meal.get().getUser().equals(user)) {
            meal.get().setMealName(mealName);
            meal.get().setMealDateTime(mealDate);
            meal.get().setCalories(mealCalories);
            mealRepository.save(meal.get());
        }
    }

    @GetMapping("/getMeal")
    public JSONObject getMeal(Authentication authentication, @RequestParam("mealId") String mealId) {
        // todo: validate parameters

        // once validated delegate to addMeal
        Long Id = Long.parseLong(mealId);
        return getMeal(getCurrentUser(authentication), Id);
    }

    public JSONObject getMeal(User user, Long mealId) {
        Optional<Meal> meal = mealRepository.findById(mealId);
        if (!meal.isPresent() || !meal.get().getUser().equals(user)) return null;
        JSONObject obj = new JSONObject();
        obj.put("mealId", meal.get().getId());
        obj.put("mealName", meal.get().getMealName());
        obj.put("mealDate", meal.get().getMealDateTime().getTime());
        obj.put("mealCalories", meal.get().getCalories());
        return obj;
    }

    @GetMapping("/getMeals")
    public JSONArray getMeals(Authentication authentication,
                              @RequestParam(value = "startDate", required = false)
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  Date startDate,
                              @RequestParam(value = "stopDate", required = false)
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date stopDate){

        // todo: validate parameters
        if (startDate != null) System.out.println(startDate.toString());
        if (stopDate != null) System.out.println(stopDate.toString());
        User user = getCurrentUser(authentication);
        return getMealsAsJson(user, startDate, stopDate);
    }

    public JSONArray getMealsAsJson(User user, Date startDate, Date stopDate) {
        List<Meal> meals = new ArrayList<>();
        if (startDate == null || stopDate == null) {
            meals.addAll(user.getMeals());
        } else {
            meals.addAll(user.getMeals(startDate, stopDate));
        }

        //sort meals
        meals.sort(new Comparator<Meal>() {
            @Override
            public int compare(Meal meal1, Meal meal2) {
                if (meal1.getMealDateTime().equals(meal2.getMealDateTime()))
                    // return the later edited event
                    return meal2.getLastModifiedAt().compareTo(meal1.getLastModifiedAt());
                // return the later occurred event
                return meal2.getMealDateTime().compareTo(meal1.getMealDateTime());
            }
        });

        JSONArray mealsJson = new JSONArray();
        for (Meal meal: meals) {
            JSONObject obj = new JSONObject();
            obj.put("mealId", meal.getId());
            obj.put("mealName", meal.getMealName());
            obj.put("mealDate", meal.getMealDateTime().getTime());
            obj.put("mealCalories", meal.getCalories());
            mealsJson.add(obj);
            // todo: sort JsonArray at frontend too
        }
        return mealsJson;
    }
}
