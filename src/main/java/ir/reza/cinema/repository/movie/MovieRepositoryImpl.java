package ir.reza.cinema.repository.movie;

import ir.reza.cinema.entity.Movie;
import ir.reza.cinema.entity.MovieStatus;
import ir.reza.cinema.repository.base.BaseRepositoryImpl;

import java.util.List;

public class MovieRepositoryImpl
        extends BaseRepositoryImpl<Movie, Long>
        implements MovieRepository {

    @Override
    protected Class<Movie> getEntityClass() {
        return Movie.class;
    }

    @Override
    protected Long getEntityId(Movie movie) {
        return movie.getId();
    }

    @Override
    protected void updateFields(
            Movie existingMovie,
            Movie newMovie
    ) {
        existingMovie.setTitle(newMovie.getTitle());
        existingMovie.setPrice(newMovie.getPrice());
        existingMovie.setStatus(newMovie.getStatus());
    }

    @Override
    public List<Movie> findByTitle(String title) {

        return hibernateUtil.execute(entityManager ->
                entityManager.createQuery(
                                "SELECT m FROM Movie m WHERE m.title = :title",
                                Movie.class
                        )
                        .setParameter("title", title)
                        .getResultList()
        );
    }

    @Override
    public List<Movie> findAvailableMovies() {

        return hibernateUtil.execute(entityManager ->
                entityManager.createNamedQuery(
                                "Movie.findAvailable",
                                Movie.class
                        )
                        .setParameter(
                                "status",
                                MovieStatus.AVAILABLE
                        )
                        .getResultList()
        );
    }

    @Override
    public List<Movie> findMoviesPurchasedByCustomer(
            Long customerId
    ) {

        return hibernateUtil.execute(entityManager ->
                entityManager.createQuery(
                                """
                                        SELECT DISTINCT m
                                        FROM Movie m
                                        JOIN Ticket t ON t.movie = m
                                        JOIN Customer c ON t.customer = c
                                        WHERE c.id = :customerId
                                        """,
                                Movie.class
                        )
                        .setParameter("customerId", customerId)
                        .getResultList()
        );
    }
}