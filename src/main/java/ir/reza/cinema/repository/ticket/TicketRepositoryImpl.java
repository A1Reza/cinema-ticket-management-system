package ir.reza.cinema.repository.ticket;

import ir.reza.cinema.entity.Ticket;
import ir.reza.cinema.repository.base.BaseRepositoryImpl;

public class TicketRepositoryImpl
        extends BaseRepositoryImpl<Ticket, Long>
        implements TicketRepository {

    @Override
    protected Class<Ticket> getEntityClass() {
        return Ticket.class;
    }

    @Override
    protected Long getEntityId(Ticket ticket) {
        return ticket.getId();
    }

    @Override
    protected void updateFields(
            Ticket existingTicket,
            Ticket newTicket
    ) {
        existingTicket.setPurchaseDate(
                newTicket.getPurchaseDate()
        );

        existingTicket.setCustomer(
                newTicket.getCustomer()
        );

        existingTicket.setMovie(
                newTicket.getMovie()
        );
    }
}