package org.delarosa.app.modules.clinico.dtos;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LineaPagoResponse(
        Integer folioPago,
        LocalDateTime fechaPago,
        BigDecimal montoPagado,
        Boolean ordenPagada,
        Integer idOrdenPago,
        BigDecimal montoTotalOrden,
        BigDecimal totalPagadoHastaAhora
) {}