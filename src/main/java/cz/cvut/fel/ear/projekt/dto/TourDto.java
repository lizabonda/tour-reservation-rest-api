package cz.cvut.fel.ear.projekt.dto;

import cz.cvut.fel.ear.projekt.model.Accommodation;

import java.time.LocalDate;
import java.util.List;

public record TourDto(Long id,
                      String destination,
                      LocalDate startDate,
                      LocalDate endDate,
                      int capacity,
                      double price,
                      List<AccommodationDto> accommodations
                      ) {}
