package org.delarosa.app.modules.farmacia.dtos;

import java.math.BigDecimal;

public record ServicioExtraEditRequest(String nombre, String descripcion, BigDecimal precio, boolean activo) {
}
