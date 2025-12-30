package org.delarosa.app.modules.farmacia.dtos;

import java.math.BigDecimal;

public record ServicioRegistroRequest(String nombre, BigDecimal precio) {
}
