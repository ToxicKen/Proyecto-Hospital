package org.delarosa.app.modules.clinico.dtos;

import java.math.BigDecimal;

public record LineaPagoRequest(Integer folioCita, BigDecimal montoPago) {
}
