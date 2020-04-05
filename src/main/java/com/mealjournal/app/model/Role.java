package com.mealjournal.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role extends Auditable {
    @NotBlank
    @Column(unique = true)
    @Getter
    @Setter
    private String name;

    @NotBlank
    @Getter
    @Setter
    private String description;

    @ManyToMany(mappedBy = "roles") // refers to roles attribute of client class
    @JsonIdentityReference // todo: change to back-reference
    private Set<User> users;
}
