package cz.cvut.fel.ear.projekt.controller;

import cz.cvut.fel.ear.projekt.dto.BookingDto;
import cz.cvut.fel.ear.projekt.dto.mapper.BookingMapper;
import cz.cvut.fel.ear.projekt.model.Booking;
import cz.cvut.fel.ear.projekt.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/bookings")
class BookingController {

    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    BookingController(BookingService bookingService, BookingMapper bookingMapper) {
        this.bookingService = bookingService;
        this.bookingMapper = bookingMapper;
    }

    @GetMapping("/{id}")
    ResponseEntity<BookingDto> getById(@PathVariable Long id) {
        Booking booking = bookingService.findById(id);
        return ResponseEntity.ok(bookingMapper.bookingToBookingDto(booking));
    }

    @PostMapping
    ResponseEntity<BookingDto> create(@RequestBody BookingDto request) {
        Booking created = bookingService.createBookingFromDto(request);
        BookingDto response = bookingMapper.bookingToBookingDto(created);
        return ResponseEntity
                .created(URI.create("/api/bookings/" + created.getId()))
                .body(response);
    }
}
