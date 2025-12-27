package cz.cvut.fel.ear.projekt.dto;
import java.time.LocalDate;

public record TourDto(Long id,
                      String destination,
                      LocalDate startDate,
                      LocalDate endDate,
                      int capacity,
                      double price
                      ) {}
