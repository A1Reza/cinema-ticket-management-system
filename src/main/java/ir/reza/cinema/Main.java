package ir.reza.cinema;

import ir.reza.cinema.adapter.MovieRepositoryAdapter;
import ir.reza.cinema.adapter.OldMovieService;
import ir.reza.cinema.chain.CustomerValidationHandler;
import ir.reza.cinema.chain.MovieAvailabilityHandler;
import ir.reza.cinema.chain.TicketCreationHandler;
import ir.reza.cinema.chain.TicketHandler;
import ir.reza.cinema.chain.TicketRequest;
import ir.reza.cinema.entity.Customer;
import ir.reza.cinema.entity.Movie;
import ir.reza.cinema.entity.MovieStatus;
import ir.reza.cinema.entity.Ticket;
import ir.reza.cinema.proxy.MovieRepositoryProxy;
import ir.reza.cinema.repository.customer.CustomerRepository;
import ir.reza.cinema.repository.customer.CustomerRepositoryImpl;
import ir.reza.cinema.repository.movie.MovieRepository;
import ir.reza.cinema.repository.movie.MovieRepositoryImpl;
import ir.reza.cinema.repository.ticket.TicketRepository;
import ir.reza.cinema.repository.ticket.TicketRepositoryImpl;

public class Main {

    public static void main(String[] args) {

        System.out.println("""
                
                ==========================================
                    CINEMA TICKET MANAGEMENT SYSTEM
                ==========================================
                """);

        // ==================================================
        // 1. Initialize Repositories
        // ==================================================

        MovieRepository realMovieRepository =
                new MovieRepositoryImpl();

        MovieRepository movieRepository =
                new MovieRepositoryProxy(
                        realMovieRepository
                );

        CustomerRepository customerRepository =
                new CustomerRepositoryImpl();

        TicketRepository ticketRepository =
                new TicketRepositoryImpl();

        System.out.println("[1] Repositories initialized successfully.");


        // ==================================================
        // 2. Adapter Pattern
        // ==================================================

        OldMovieService oldMovieService =
                new OldMovieService(realMovieRepository);

        MovieRepository movieRepositoryAdapter =
                new MovieRepositoryAdapter(
                        oldMovieService
                );

        System.out.println("[2] Adapter initialized successfully.");


        // ==================================================
        // 3. Create Movies
        // ==================================================

        Movie movie1 = new Movie();

        movie1.setTitle("Interstellar");
        movie1.setPrice(15.99);
        movie1.setStatus(MovieStatus.AVAILABLE);


        Movie movie2 = new Movie();

        movie2.setTitle("Inception");
        movie2.setPrice(12.99);
        movie2.setStatus(MovieStatus.NOT_AVAILABLE);


        movieRepository.save(movie1);
        movieRepository.save(movie2);

        System.out.println("""
                
                [3] Movies created:
                    - Interstellar
                    - Inception
                """);


        // ==================================================
        // 4. Create Customers
        // ==================================================

        Customer customer1 = new Customer();

        customer1.setName("Reza");
        customer1.setEmail("reza@example.com");


        Customer customer2 = new Customer();

        customer2.setName("Ali");
        customer2.setEmail("ali@example.com");


        customerRepository.save(customer1);
        customerRepository.save(customer2);

        System.out.println("""
                [4] Customers created:
                    - Reza
                    - Ali
                """);


        // ==================================================
        // 5. Find Movie by ID
        // ==================================================

        Movie foundMovie =
                movieRepository
                        .findById(movie1.getId())
                        .orElseThrow();

        System.out.println("""
                [5] Movie found by ID:
                """);

        System.out.println("    ID     : " + foundMovie.getId());
        System.out.println("    Title  : " + foundMovie.getTitle());
        System.out.println("    Price  : " + foundMovie.getPrice());
        System.out.println("    Status : " + foundMovie.getStatus());


        // ==================================================
        // 6. Update Movie Price
        // ==================================================

        foundMovie.setPrice(17.99);

        movieRepository.update(foundMovie);

        System.out.println("""
                
                [6] Movie price updated:
                """);

        System.out.println(
                "    New Price : " + foundMovie.getPrice()
        );


        // ==================================================
        // 7. Find Available Movies
        // ==================================================

        System.out.println("""
                
                [7] Available Movies
                ------------------------------------------
                """);

        movieRepository
                .findAvailableMovies()
                .forEach(movie -> {

                    System.out.println(
                            "    ID     : " + movie.getId()
                    );

                    System.out.println(
                            "    Title  : " + movie.getTitle()
                    );

                    System.out.println(
                            "    Price  : " + movie.getPrice()
                    );

                    System.out.println(
                            "    Status : " + movie.getStatus()
                    );

                    System.out.println(
                            "    ------------------------------------------"
                    );
                });


        // ==================================================
        // 8. Adapter Pattern Test
        // ==================================================

        Movie adaptedMovie =
                movieRepositoryAdapter
                        .findById(movie1.getId())
                        .orElseThrow();

        System.out.println("""
                
                [8] Adapter Pattern
                ------------------------------------------
                """);

        System.out.println(
                "    Old Service Method : getMovie(id)"
        );

        System.out.println(
                "    New Interface      : findById(id)"
        );

        System.out.println(
                "    Adapted Movie      : "
                        + adaptedMovie.getTitle()
        );


        // ==================================================
        // 9. Build Chain of Responsibility
        // ==================================================

        TicketHandler movieAvailabilityHandler =
                new MovieAvailabilityHandler(
                        movieRepository
                );

        TicketHandler customerValidationHandler =
                new CustomerValidationHandler(
                        customerRepository
                );

        TicketHandler ticketCreationHandler =
                new TicketCreationHandler(
                        movieRepository,
                        customerRepository,
                        ticketRepository
                );


        movieAvailabilityHandler.setNext(
                customerValidationHandler
        );

        customerValidationHandler.setNext(
                ticketCreationHandler
        );

        System.out.println("""
                
                [9] Ticket Purchase Chain
                ------------------------------------------
                    Ticket Request
                         |
                         v
                    Movie Availability
                         |
                         v
                    Customer Validation
                         |
                         v
                    Ticket Creation
                ------------------------------------------
                """);


        // ==================================================
        // 10. Purchase Ticket
        // ==================================================

        TicketRequest request =
                new TicketRequest(
                        movie1.getId(),
                        customer1.getId()
                );

        Ticket ticket =
                movieAvailabilityHandler.handle(request);


        // ==================================================
        // 11. Print Created Ticket
        // ==================================================

        System.out.println("""
                
                [10] Ticket Created Successfully
                ==========================================
                """);

        System.out.println(
                "    Ticket ID      : "
                        + ticket.getId()
        );

        System.out.println(
                "    Purchase Date  : "
                        + ticket.getPurchaseDate()
        );

        System.out.println(
                "    Customer       : "
                        + ticket.getCustomer().getName()
        );

        System.out.println(
                "    Customer Email : "
                        + ticket.getCustomer().getEmail()
        );

        System.out.println(
                "    Movie          : "
                        + ticket.getMovie().getTitle()
        );

        System.out.println(
                "    Movie Price    : "
                        + ticket.getMovie().getPrice()
        );

        System.out.println(
                "    Movie Status   : "
                        + ticket.getMovie().getStatus()
        );


        // ==================================================
        // 12. Finish
        // ==================================================

        System.out.println("""
                
                ==========================================
                    OPERATION COMPLETED SUCCESSFULLY
                ==========================================
                """);
    }
}