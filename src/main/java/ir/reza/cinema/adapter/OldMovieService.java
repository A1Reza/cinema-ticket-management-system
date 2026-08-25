package ir.reza.cinema.adapter;

import ir.reza.cinema.entity.Movie;
import ir.reza.cinema.repository.movie.MovieRepository;

public class OldMovieService {

    private final MovieRepository movieRepository;

    public OldMovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie getMovie(Long id) {
        return movieRepository.findById(id)
                .orElse(null);
    }
}