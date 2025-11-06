package cz.cvut.fel.ear.projekt.dao;

import cz.cvut.fel.ear.projekt.model.Accomodation;
import cz.cvut.fel.ear.projekt.model.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class PersonDao implements GenericDao<Person> {
    @PersistenceContext
    protected EntityManager em;

    @Override
    public Person find(Integer id) {
        Objects.requireNonNull(id);
        return em.find(Person.class, id);
    }

    @Override
    public List<Person> findAll() {
        return em.createQuery("SELECT p FROM Person p", Person.class).getResultList();
    }

    @Override
    public void persist(Person entity) {
        Objects.requireNonNull(entity);
        em.persist(entity);
    }

    @Override
    public Person update(Person entity) {
        Objects.requireNonNull(entity);
        return em.merge(entity);
    }

    @Override
    public void remove(Person entity) {
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
