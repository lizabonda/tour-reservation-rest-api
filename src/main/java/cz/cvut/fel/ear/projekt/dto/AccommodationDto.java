package cz.cvut.fel.ear.projekt.dto;

import cz.cvut.fel.ear.projekt.model.MealPlan;


public record AccommodationDto(Long id,
                               String name,
                               String city,
                               String address,
                               int stars,
                               String roomType,
                               int capacity,
                               double pricePerNight,
                               MealPlan mealPlan
) {}
