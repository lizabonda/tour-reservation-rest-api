package cz.cvut.fel.ear.projekt.service;

import cz.cvut.fel.ear.projekt.dao.BookingDao;
import cz.cvut.fel.ear.projekt.model.Booking;
import cz.cvut.fel.ear.projekt.model.Tour;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class TourService {

    private final BookingDao bookingDao;

    public TourService(BookingDao bookingDao) {
        this.bookingDao = bookingDao;
    }

    public void validateCapacity(Tour tour, int requestedSize) {
        Objects.requireNonNull(tour);
        int capacity = tour.getCapacity();
        int occupied= bookingDao.countPersonsByTour(tour.getId());

    if(requestedSize+occupied>capacity) {
        throw new IllegalStateException(
            "Tour capacity exceeded: capacity=" + capacity
    );
}
    }
}
