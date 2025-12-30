package org.delarosa.app.modules.farmacia.services;


import org.delarosa.app.modules.farmacia.dtos.PagoResponse;
import org.delarosa.app.modules.farmacia.dtos.TicketRequest;
import org.delarosa.app.modules.farmacia.dtos.TicketResponse;
import org.delarosa.app.modules.farmacia.entities.Ticket;


import java.util.Optional;

public interface TicketService {
    TicketResponse createTicket(TicketRequest dto);

    Ticket obtenerTicketById(Integer id);

    void pagarTicket(PagoResponse pago);

    byte[] obtenerPDFTicket(Integer idTicket);
}
