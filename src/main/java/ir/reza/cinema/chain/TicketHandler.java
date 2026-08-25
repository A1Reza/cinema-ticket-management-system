package ir.reza.cinema.chain;

import ir.reza.cinema.entity.Ticket;

public interface TicketHandler {

    void setNext(TicketHandler next);

    Ticket handle(TicketRequest request);
}