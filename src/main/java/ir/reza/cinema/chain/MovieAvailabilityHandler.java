package ir.reza.cinema.chain;

import ir.reza.cinema.entity.Movie;
import ir.reza.cinema.entity.MovieStatus;
import ir.reza.cinema.entity.Ticket;
import ir.reza.cinema.repository.movie.MovieRepository;

public class MovieAvailabilityHandler
        implements TicketHandler {

    private final MovieRepository movieRepository;

    private TicketHandler next;

    public MovieAvailabilityHandler(
            MovieRepository movieRepository
    ) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void setNext(TicketHandler next) {
        this.next = next;
    }

    @Override
    public Ticket handle(TicketRequest request) {

        Movie movie = movieRepository
                .findById(request.getMovieId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Movie not found with id: "
                                        + request.getMovieId()
                        )
                );

        if (movie.getStatus() != MovieStatus.AVAILABLE) {
            throw new IllegalStateException(
                    "Movie is not available"
            );
        }

        if (next != null) {
            return next.handle(request);
        }

        return null;
    }
}