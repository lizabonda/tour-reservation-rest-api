package cz.cvut.fel.ear.projekt.dto;


public record AccommodationDto(Long id,
                               String name,
                               String address,
                               double pricePerNight,
                               String roomType
) {}
