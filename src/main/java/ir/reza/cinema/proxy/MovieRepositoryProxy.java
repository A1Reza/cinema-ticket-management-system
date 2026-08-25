package ir.reza.cinema.proxy;

import ir.reza.cinema.entity.Movie;
import ir.reza.cinema.repository.movie.MovieRepository;

import java.util.List;
import java.util.Optional;

public class MovieRepositoryProxy implements MovieRepository {

    private final MovieRepository movieRepository;

    public MovieRepositoryProxy(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie save(Movie movie) {

        if (movie == null) {
            throw new IllegalArgumentException(
                    "Movie cannot be null"
            );
        }

        return movieRepository.save(movie);
    }

    @Override
    public Optional<Movie> findById(Long id) {

        validateId(id);

        return movieRepository.findById(id);
    }

    @Override
    public List<Movie> findAll() {

        return movieRepository.findAll();
    }

    @Override
    public Movie update(Movie movie) {

        if (movie == null) {
            throw new IllegalArgumentException(
                    "Movie cannot be null"
            );
        }

        validateId(movie.getId());

        return movieRepository.update(movie);
    }

    @Override
    public void delete(Movie movie) {

        if (movie == null) {
            throw new IllegalArgumentException(
                    "Movie cannot be null"
            );
        }

        validateId(movie.getId());

        movieRepository.delete(movie);
    }

    @Override
    public List<Movie> findByTitle(String title) {

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(
                    "Movie title cannot be null or blank"
            );
        }

        return movieRepository.findByTitle(title);
    }

    @Override
    public List<Movie> findAvailableMovies() {

        return movieRepository.findAvailableMovies();
    }

    @Override
    public List<Movie> findMoviesPurchasedByCustomer(
            Long customerId
    ) {

        validateId(customerId);

        return movieRepository.findMoviesPurchasedByCustomer(
                customerId
        );
    }

    private void validateId(Long id) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "Movie ID cannot be null"
            );
        }
    }
}