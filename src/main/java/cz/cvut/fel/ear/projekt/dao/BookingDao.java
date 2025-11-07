package cz.cvut.fel.ear.projekt.dao;

import cz.cvut.fel.ear.projekt.model.Accommodation;
import cz.cvut.fel.ear.projekt.model.Booking;
import cz.cvut.fel.ear.projekt.model.Tour;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class BookingDao implements GenericDao<Booking> {
    @PersistenceContext
    protected EntityManager em;

    @Override
    public Booking find(Long id) {
        Objects.requireNonNull(id);
        return em.find(Booking.class, id);
    }

    @Override
    public List<Booking> findAll() {
        return em.createQuery("SELECT b FROM Booking b", Booking.class).getResultList();
    }

    @Override
    public void save(Booking entity) {
        Objects.requireNonNull(entity);
        em.persist(entity);
    }

    @Override
    public Booking update(Booking entity) {
        Objects.requireNonNull(entity);
        return em.merge(entity);
    }

    @Override
    public void remove(Booking entity) {
        Objects.requireNonNull(entity);
        if (em.contains(entity)) {
            em.remove(entity);
            return;
        }
        final Booking toRemove = em.find(Booking.class, entity.getId());
        if (toRemove != null) {
            em.remove(toRemove);
        }
    }

    public int countPersonsByTour(Long tourId) {
        Long count = em.createQuery(
                        "SELECT COUNT(p) " +
                                "FROM Booking b " +
                                "JOIN b.persons p " +
                                "WHERE b.tour.id = :tourId",
                        Long.class
                )
                .setParameter("tourId", tourId)
                .getSingleResult();

        return count.intValue();
    }
}
