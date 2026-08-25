package ir.reza.cinema.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.function.Consumer;
import java.util.function.Function;

public final class HibernateUtil {

    private static final String PERSISTENCE_UNIT = "cinema-ticket-unit";

    private static HibernateUtil INSTANCE;

    private final EntityManagerFactory entityManagerFactory;

    private HibernateUtil() {
        this.entityManagerFactory =
                Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
    }

    public static synchronized HibernateUtil getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new HibernateUtil();
        }

        return INSTANCE;
    }

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public void executeInTransaction(Consumer<EntityManager> action) {

        EntityManager entityManager = getEntityManager();

        try {
            entityManager.getTransaction().begin();

            action.accept(entityManager);

            entityManager.getTransaction().commit();

        } catch (Exception e) {

            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            throw e;

        } finally {
            entityManager.close();
        }
    }

    public <T> T executeInTransactionWithResult(
            Function<EntityManager, T> action) {

        EntityManager entityManager = getEntityManager();

        try {
            entityManager.getTransaction().begin();

            T result = action.apply(entityManager);

            entityManager.getTransaction().commit();

            return result;

        } catch (Exception e) {

            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            throw e;

        } finally {
            entityManager.close();
        }
    }

    public <T> T execute(Function<EntityManager, T> action) {

        EntityManager entityManager = getEntityManager();

        try {
            return action.apply(entityManager);
        } finally {
            entityManager.close();
        }
    }
}