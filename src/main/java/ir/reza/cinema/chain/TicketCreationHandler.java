package ir.reza.cinema.chain;

import ir.reza.cinema.entity.Customer;
import ir.reza.cinema.entity.Movie;
import ir.reza.cinema.entity.Ticket;
import ir.reza.cinema.repository.customer.CustomerRepository;
import ir.reza.cinema.repository.movie.MovieRepository;
import ir.reza.cinema.repository.ticket.TicketRepository;

import java.time.LocalDateTime;

public class TicketCreationHandler
        implements TicketHandler {

    private final MovieRepository movieRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;

    public TicketCreationHandler(
            MovieRepository movieRepository,
            CustomerRepository customerRepository,
            TicketRepository ticketRepository
    ) {
        this.movieRepository = movieRepository;
        this.customerRepository = customerRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void setNext(TicketHandler next) {
        //Last handler in the chain.
    }

    @Override
    public Ticket handle(TicketRequest request) {

        Movie movie = movieRepository
                .findById(request.getMovieId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Movie not found"
                        )
                );

        Customer customer = customerRepository
                .findById(request.getCustomerId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found"
                        )
                );

        Ticket ticket = new Ticket();

        ticket.setMovie(movie);
        ticket.setCustomer(customer);
        ticket.setPurchaseDate(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }
}