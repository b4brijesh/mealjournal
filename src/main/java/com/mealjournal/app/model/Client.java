package com.mealjournal.app.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
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
    @Getter @Setter //todo: custom setter below
    private String saltedHashedPass;

    /*public void setSaltedHashedPassword(String password) {
        saltedHashedPassword = new BCryptPasswordEncoder(8).encode(password);
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @Getter @Setter
    Set<Role> roles = new HashSet<>();

    public Client() {}

    //copy constructor
    public Client(Client user) {
        email = user.getEmail();
        saltedHashedPassword = user.getSaltedHashedPassword();
        roles = user.getRoles();
    }*/

}
