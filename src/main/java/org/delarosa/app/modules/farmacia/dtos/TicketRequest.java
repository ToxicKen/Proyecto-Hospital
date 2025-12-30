package org.delarosa.app.modules.farmacia.dtos;

import java.util.List;

public record TicketRequest(Integer idEmpleado, List<MedicamentoTicketRequest> medicamentos, List<ServicioTicketRequest> servicios) {
}
