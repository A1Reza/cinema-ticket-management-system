package ir.reza.cinema.repository.movie;

import ir.reza.cinema.entity.Movie;
import ir.reza.cinema.entity.MovieStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MovieRepositoryTest {

    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        movieRepository = new MovieRepositoryImpl();
    }

    @Test
    void testSaveAndFindMovie() {

        Movie movie = new Movie();

        movie.setTitle("Interstellar");
        movie.setPrice(15.99);
        movie.setStatus(MovieStatus.AVAILABLE);

        Movie savedMovie = movieRepository.save(movie);

        Optional<Movie> foundMovie =
                movieRepository.findById(savedMovie.getId());

        assertTrue(foundMovie.isPresent());

        Movie result = foundMovie.get();

        assertEquals("Interstellar", result.getTitle());

        assertEquals(
                15.99,
                result.getPrice(),
                0.001
        );

        assertEquals(
                MovieStatus.AVAILABLE,
                result.getStatus()
        );
    }

    @Test
    void testFindAvailableMovies() {

        Movie availableMovie = new Movie();

        availableMovie.setTitle("Interstellar");
        availableMovie.setPrice(15.99);
        availableMovie.setStatus(
                MovieStatus.AVAILABLE
        );

        Movie unavailableMovie = new Movie();

        unavailableMovie.setTitle("Inception");
        unavailableMovie.setPrice(12.99);
        unavailableMovie.setStatus(
                MovieStatus.NOT_AVAILABLE
        );

        movieRepository.save(availableMovie);
        movieRepository.save(unavailableMovie);

        List<Movie> availableMovies =
                movieRepository.findAvailableMovies();

        assertTrue(
                availableMovies.stream()
                        .anyMatch(movie ->
                                movie.getTitle()
                                        .equals("Interstellar")
                        )
        );

        assertFalse(
                availableMovies.stream()
                        .anyMatch(movie ->
                                movie.getTitle()
                                        .equals("Inception")
                        )
        );
    }
}