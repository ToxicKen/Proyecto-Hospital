package org.delarosa.app.modules.farmacia.dtos;



import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record TicketResponse(Integer idTicket, LocalDateTime fechaVenta, String estatus, BigDecimal total, List<DetalleMedicamentoResponse> medicamentos, List<DetalleServicioExtraResponse> servicios) {
}
