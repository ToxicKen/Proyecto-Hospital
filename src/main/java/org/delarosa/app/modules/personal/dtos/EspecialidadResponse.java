package org.delarosa.app.modules.personal.dtos;

import java.math.BigDecimal;

public record EspecialidadResponse(Integer idEspecialidad, String nombre, BigDecimal costo) {
}
