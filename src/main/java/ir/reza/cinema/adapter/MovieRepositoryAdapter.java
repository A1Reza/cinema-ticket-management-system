package ir.reza.cinema.adapter;

import ir.reza.cinema.entity.Movie;
import ir.reza.cinema.repository.movie.MovieRepository;

import java.util.List;
import java.util.Optional;

public class MovieRepositoryAdapter implements MovieRepository {

    private final OldMovieService oldMovieService;

    public MovieRepositoryAdapter(OldMovieService oldMovieService) {
        this.oldMovieService = oldMovieService;
    }

    @Override
    public Optional<Movie> findById(Long id) {

        return Optional.ofNullable(
                oldMovieService.getMovie(id)
        );
    }

    @Override
    public Movie save(Movie movie) {
        throw new UnsupportedOperationException(
                "Save is not supported by old movie service"
        );
    }

    @Override
    public List<Movie> findAll() {
        throw new UnsupportedOperationException(
                "Find all is not supported by old movie service"
        );
    }

    @Override
    public Movie update(Movie movie) {
        throw new UnsupportedOperationException(
                "Update is not supported by old movie service"
        );
    }

    @Override
    public void delete(Movie movie) {
        throw new UnsupportedOperationException(
                "Delete is not supported by old movie service"
        );
    }

    @Override
    public List<Movie> findByTitle(String title) {
        throw new UnsupportedOperationException(
                "Find by title is not supported by old movie service"
        );
    }

    @Override
    public List<Movie> findAvailableMovies() {
        throw new UnsupportedOperationException(
                "Find available movies is not supported by old movie service"
        );
    }

    @Override
    public List<Movie> findMoviesPurchasedByCustomer(
            Long customerId
    ) {
        throw new UnsupportedOperationException(
                "Find movies purchased by customer is not supported by old movie service"
        );
    }
}