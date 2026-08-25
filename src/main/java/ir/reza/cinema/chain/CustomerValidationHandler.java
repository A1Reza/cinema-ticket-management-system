package ir.reza.cinema.chain;

import ir.reza.cinema.repository.customer.CustomerRepository;
import ir.reza.cinema.entity.Ticket;

public class CustomerValidationHandler implements TicketHandler {

    private final CustomerRepository customerRepository;

    private TicketHandler next;

    public CustomerValidationHandler(
            CustomerRepository customerRepository
    ) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void setNext(TicketHandler next) {
        this.next = next;
    }

    @Override
    public Ticket handle(TicketRequest request) {

        customerRepository
                .findById(request.getCustomerId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found with id: "
                                        + request.getCustomerId()
                        )
                );

        if (next != null) {
            return next.handle(request);
        }

        return null;
    }
}