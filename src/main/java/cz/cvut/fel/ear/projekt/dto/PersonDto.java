package cz.cvut.fel.ear.projekt.dto;

import java.time.LocalDate;

public record PersonDto(Long id,
                        String firstName,
                        String lastName,
                        LocalDate dateOfBirth
                        ) {}
