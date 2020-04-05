package com.mealjournal.app.repositories;

import com.mealjournal.app.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);
    // Optional is a monad
}
