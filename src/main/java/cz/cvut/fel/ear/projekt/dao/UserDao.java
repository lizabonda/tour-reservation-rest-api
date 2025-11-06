package cz.cvut.fel.ear.projekt.dao;

import cz.cvut.fel.ear.projekt.model.Accomodation;
import cz.cvut.fel.ear.projekt.model.Tour;
import cz.cvut.fel.ear.projekt.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class UserDao implements GenericDao<User> {
    @PersistenceContext
    protected EntityManager em;


    @Override
    public User find(Integer id) {
        Objects.requireNonNull(id);
        return em.find(User.class, id);
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public void persist(User entity) {
        Objects.requireNonNull(entity);
        em.persist(entity);
    }

    @Override
    public User update(User entity) {
        Objects.requireNonNull(entity);
        return em.merge(entity);
    }

    @Override
    public void remove(User entity) {
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
