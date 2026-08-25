package ir.reza.cinema.repository.base;

import ir.reza.cinema.util.HibernateUtil;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public abstract class BaseRepositoryImpl<T, ID>
        implements BaseRepository<T, ID> {

    protected final HibernateUtil hibernateUtil;

    protected BaseRepositoryImpl() {
        this.hibernateUtil = HibernateUtil.getInstance();
    }

    protected abstract Class<T> getEntityClass();

    protected abstract ID getEntityId(T entity);

    protected abstract void updateFields(
            T existingEntity,
            T newEntity
    );

    @Override
    public T save(T entity) {

        hibernateUtil.executeInTransaction(entityManager ->
                entityManager.persist(entity)
        );

        return entity;
    }

    @Override
    public Optional<T> findById(ID id) {

        EntityManager entityManager =
                hibernateUtil.getEntityManager();

        try {
            return Optional.ofNullable(
                    entityManager.find(getEntityClass(), id)
            );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<T> findAll() {

        EntityManager entityManager =
                hibernateUtil.getEntityManager();

        try {
            return entityManager.createQuery(
                    "SELECT e FROM "
                            + getEntityClass().getSimpleName()
                            + " e",
                    getEntityClass()
            ).getResultList();

        } finally {
            entityManager.close();
        }
    }

    @Override
    public T update(T entity) {

        ID id = getEntityId(entity);

        T existingEntity = findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Entity not found with id: " + id
                        )
                );

        hibernateUtil.executeInTransaction(entityManager ->
                updateFields(existingEntity, entity)
        );

        return existingEntity;
    }

    @Override
    public void delete(T entity) {

        hibernateUtil.executeInTransaction(entityManager ->
                entityManager.remove(entity)
        );
    }
}