package com.mealjournal.app.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Client extends Auditable{

    @Email
    @NotBlank
    @Column(unique = true)
    @Getter
    @Setter
    private String email;

    @NotBlank
    @Getter //todo:
    private String saltedHashedPass;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonManagedReference
    @Getter
    @Setter
    private Set<Role> roles;

    // custom password encryption setter below
    public void setSaltedHashedPass(String rawPassword) {
        saltedHashedPass = new BCryptPasswordEncoder(6).encode(rawPassword);
    }

    public Client() {}

    //copy constructor for userDetails in authentication
    public Client(Client client) {
        email = client.getEmail();
        saltedHashedPass = client.getSaltedHashedPass();
        roles = client.getRoles();
    }

}
