package com.mealjournal.app.auth;

import com.mealjournal.app.model.Client;
import com.mealjournal.app.model.Role;
import com.mealjournal.app.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private ClientRepository clientRepository;


    @Override
    public UserDetails loadUserByUsername(String clientEmail) {
        Client client = clientRepository.findByEmail(clientEmail).orElseThrow(null);
        // todo: throw username not found exception

        return new CustomUserDetails(client);
    }
}
