package org.delarosa.app.modules.farmacia.dtos;

import java.math.BigDecimal;

public record ServicioExtraResponse(Integer idServicioExtra, String nombre, String descripcion,BigDecimal precio,Boolean activo) {
}
