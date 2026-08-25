package ir.reza.cinema.repository.movie;

import ir.reza.cinema.entity.Movie;
import ir.reza.cinema.repository.base.BaseRepository;

import java.util.List;

public interface MovieRepository extends BaseRepository<Movie, Long> {

    List<Movie> findByTitle(String title);

    List<Movie> findAvailableMovies();

    List<Movie> findMoviesPurchasedByCustomer(Long customerId);
}