package cz.cvut.fel.ear.projekt.dao;

import cz.cvut.fel.ear.projekt.model.Accomodation;
import cz.cvut.fel.ear.projekt.model.Tour;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class TourDao implements  GenericDao<Tour> {
    @PersistenceContext
    protected EntityManager em;

    @Override
    public Tour find(Integer id) {
        Objects.requireNonNull(id);
        return em.find(Tour.class, id);
    }

    @Override
    public List<Tour> findAll() {
        return em.createQuery("SELECT t FROM Tour t", Tour.class).getResultList();
    }

    @Override
    public void persist(Tour entity) {
        Objects.requireNonNull(entity);
        em.persist(entity);
    }

    @Override
    public Tour update(Tour entity) {
        Objects.requireNonNull(entity);
        return em.merge(entity);
    }

    @Override
    public void remove(Tour entity) {
        Objects.requireNonNull(entity);
        if (em.contains(entity)) {
            em.remove(entity);
            return;
        }
        final Accomodation toRemove = em.find(Accomodation.class, entity.getId());
        if (toRemove != null) {
            em.remove(toRemove);
        }
    }
}
