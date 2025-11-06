package cz.cvut.fel.ear.projekt.dao;

import cz.cvut.fel.ear.projekt.model.Accomodation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Repository
public class AccomodationDao implements GenericDao<Accomodation> {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Accomodation find(Integer id) {
        Objects.requireNonNull(id);
        return em.find(Accomodation.class, id);
    }

    @Override
    public List<Accomodation> findAll() {
        return em.createQuery("SELECT a FROM Accomodation a", Accomodation.class).getResultList();
    }

    @Override
    public void persist(Accomodation entity) {
        Objects.requireNonNull(entity);
        em.persist(entity);

    }

    @Override
    public Accomodation update(Accomodation entity) {
        Objects.requireNonNull(entity);
        return em.merge(entity);
    }

    @Override
    public void remove(Accomodation entity) {
        Objects.requireNonNull(entity);
        if (em.contains(entity)) {
            em.remove(entity);
            return;
        }
        final Accomodation toRemove = em.find(Accomodation.class, entity.getId());
        if (toRemove != null) {
            em.remove(toRemove);
        }
}}
