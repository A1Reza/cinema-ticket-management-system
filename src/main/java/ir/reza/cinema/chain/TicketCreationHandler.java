package ir.reza.cinema.chain;

import ir.reza.cinema.entity.Customer;
import ir.reza.cinema.entity.Movie;
import ir.reza.cinema.entity.Ticket;
import ir.reza.cinema.util.HibernateUtil;

import java.time.LocalDateTime;

public class TicketCreationHandler
        implements TicketHandler {

    @Override
    public void setNext(TicketHandler next) {
        // Last handler in the chain.
    }

    @Override
    public Ticket handle(TicketRequest request) {

        return HibernateUtil.getInstance()
                .executeInTransactionWithResult(
                        entityManager -> {

                            Movie movie = entityManager.find(
                                    Movie.class,
                                    request.getMovieId()
                            );

                            if (movie == null) {
                                throw new IllegalArgumentException(
                                        "Movie not found"
                                );
                            }

                            Customer customer = entityManager.find(
                                    Customer.class,
                                    request.getCustomerId()
                            );

                            if (customer == null) {
                                throw new IllegalArgumentException(
                                        "Customer not found"
                                );
                            }

                            Ticket ticket = new Ticket();

                            ticket.setPurchaseDate(
                                    LocalDateTime.now()
                            );

                            customer.addTicket(ticket);
                            movie.addTicket(ticket);

                            entityManager.persist(ticket);

                            return ticket;
                        }
                );
    }
}