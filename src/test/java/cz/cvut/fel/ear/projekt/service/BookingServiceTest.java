package cz.cvut.fel.ear.projekt.service;

import cz.cvut.fel.ear.projekt.dao.BookingDao;
import cz.cvut.fel.ear.projekt.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class BookingServiceTest {

    @Mock
    private BookingDao bookingDao;

    @Mock
    private TourService tourService;

    @InjectMocks
    private BookingService bookingService;

    private Tour tour;
    private Person person;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        tour = new Tour();
        tour.setId(1L);
        tour.setCapacity(10);
        tour.setPrice(500.0);
        tour.setTitle("Summer");
        tour.setStartDate(LocalDate.now().plusDays(30));
        tour.setEndDate(LocalDate.now().plusDays(37));

        person = new Person();
        person.setFirstName("Alice");
        person.setLastName("Tester");
        person.setDateOfBirth(LocalDate.of(1990, 7, 13));
    }

    @Test
    void createBooking_ShouldThrowException_WhenBookingHasNoTour() {
        Booking booking = new Booking();
        booking.setPersons(List.of(person));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> bookingService.createBooking(booking));

        assertEquals("Booking must have a tour", ex.getMessage());
        verifyNoInteractions(tourService, bookingDao);
    }

    @Test
    void createBooking_ShouldThrowException_WhenTourIsOverbooked() {
        Booking booking = new Booking();
        booking.setTour(tour);
        booking.setPersons(List.of(person));

        doThrow(new IllegalStateException("Tour capacity exceeded")).when(tourService).validateCapacity(eq(tour), eq(1));

        IllegalStateException ex = assertThrows(IllegalStateException.class
                , () -> bookingService.createBooking(booking));

        assertEquals("Tour capacity exceeded", ex.getMessage());
        verify(tourService).validateCapacity(tour, 1);
        verify(bookingDao, never()).save(any());
    }

    @Test
    void createBooking_ShouldCalculatePrice_ValidateCapacity_AndSave() {
        Booking booking = spy(new Booking());
        booking.setTour(tour);
        booking.setPersons(List.of(person));
        booking.setCreatedAt(LocalDate.now());

        bookingService.createBooking(booking);

        verify(tourService).validateCapacity(tour, 1);
        verify(booking).saveTotalPrice();
        verify(bookingDao).save(booking);
    }


    @Test
    void createBooking_ShouldNotAcceptNullBooking() {
        assertThrows(NullPointerException.class, () -> bookingService.createBooking(null));
        verifyNoInteractions(tourService, bookingDao);
    }
}
