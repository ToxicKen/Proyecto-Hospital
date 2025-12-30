package org.delarosa.app.modules.farmacia.dtos;

import java.math.BigDecimal;

public record PagoResponse(Integer idTicket, Integer idMetodoPago, BigDecimal montoPago) {
}
