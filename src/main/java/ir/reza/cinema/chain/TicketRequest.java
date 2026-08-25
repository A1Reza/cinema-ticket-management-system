package ir.reza.cinema.chain;

public class TicketRequest {

    private final Long movieId;
    private final Long customerId;

    public TicketRequest(Long movieId, Long customerId) {
        this.movieId = movieId;
        this.customerId = customerId;
    }

    public Long getMovieId() {
        return movieId;
    }

    public Long getCustomerId() {
        return customerId;
    }
}
/*
Ticket Request
      ↓
MovieAvailabilityHandler
      ↓
CustomerValidationHandler
      ↓
Ticket Creation
*/
