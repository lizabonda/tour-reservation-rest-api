package cz.cvut.fel.ear.projekt.dto;

import cz.cvut.fel.ear.projekt.model.Accommodation;

import java.util.List;

public record PersonDto(Long id,
                        String firstName,
                        String lastName
                        ) {}
