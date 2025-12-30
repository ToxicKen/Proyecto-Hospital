package org.delarosa.app.modules.farmacia.repositories;

import org.delarosa.app.modules.farmacia.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket,Integer> {
}
