package org.delarosa.app.modules.general.dtos;

import java.util.List;

public record PersonaResponse(Integer idPersona,
                              String nombreCompleto,
                              String curp,
                              String direccion,
                              List<TelefonoDTO> telefonos) {
}
