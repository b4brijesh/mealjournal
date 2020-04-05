package com.mealjournal.app.repositories;

import com.mealjournal.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name); // Optional is a monad
}
