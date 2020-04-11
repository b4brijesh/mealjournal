package com.mealjournal.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "meals")
public class Meal extends Auditable{

    @ManyToOne
    @JsonBackReference
    @NotNull
    @Getter
    @Setter
    private User user;

    @NotBlank
    @Getter
    @Setter
    private String mealName;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Getter
    @Setter
    private Date mealDateTime; //todo: initialize to current date-time

    @NotNull
    @Getter
    @Setter
    @Range(min = 1)
    private Integer calories;

    public Meal() {}
    private Meal(Builder builder) {
        setUser(builder.user);
        setMealName(builder.mealName);
        setMealDateTime(builder.mealDateTime);
        setCalories(builder.calories);
    }

    public static final class Builder {
        private @NotNull User user;
        private @NotBlank String mealName;
        private @NotNull Date mealDateTime;
        private @NotNull @Range(min = 1) Integer calories;

        public Builder() {
        }

        public Builder user(@NotNull User val) {
            user = val;
            return this;
        }

        public Builder mealName(@NotBlank String val) {
            mealName = val;
            return this;
        }

        public Builder mealDateTime(@NotNull Date val) {
            mealDateTime = val;
            return this;
        }

        public Builder calories(@NotNull @Range(min = 1) Integer val) {
            calories = val;
            return this;
        }

        public Meal build() {
            return new Meal(this);
        }
    }
}
