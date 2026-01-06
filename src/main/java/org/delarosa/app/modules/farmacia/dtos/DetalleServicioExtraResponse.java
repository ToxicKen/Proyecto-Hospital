package org.delarosa.app.modules.farmacia.dtos;

import java.math.BigDecimal;

public record DetalleServicioExtraResponse(Integer idServicioExtra,
                                           String nombre,
                                           Integer cantidad,
                                           BigDecimal precioUnitario,
                                           BigDecimal subtotal) {
}
