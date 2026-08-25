package ir.reza.cinema.repository.base;

import ir.reza.cinema.util.HibernateUtil;

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

        return hibernateUtil.execute(entityManager ->
                Optional.ofNullable(
                        entityManager.find(getEntityClass(), id)
                )
        );
    }

    @Override
    public List<T> findAll() {

        return hibernateUtil.execute(entityManager ->
                entityManager.createQuery(
                        "SELECT e FROM "
                                + getEntityClass().getSimpleName()
                                + " e",
                        getEntityClass()
                ).getResultList()
        );
    }

    @Override
    public T update(T entity) {

        ID id = getEntityId(entity);

        return hibernateUtil.executeInTransactionWithResult(
                entityManager -> {

                    T existingEntity =
                            entityManager.find(
                                    getEntityClass(),
                                    id
                            );

                    if (existingEntity == null) {
                        throw new IllegalArgumentException(
                                "Entity not found with id: " + id
                        );
                    }

                    updateFields(
                            existingEntity,
                            entity
                    );

                    return existingEntity;
                }
        );
    }

    @Override
    public void delete(T entity) {

        hibernateUtil.executeInTransaction(entityManager -> {

            T managedEntity = entityManager.find(
                    getEntityClass(),
                    getEntityId(entity)
            );

            if (managedEntity == null) {
                throw new IllegalArgumentException(
                        "Entity not found with id: "
                                + getEntityId(entity)
                );
            }

            entityManager.remove(managedEntity);
        });
    }
}