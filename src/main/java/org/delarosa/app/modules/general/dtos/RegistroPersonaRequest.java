package org.delarosa.app.modules.general.dtos;

import java.util.List;

public record RegistroPersonaRequest(String nombre, String apellidoM, String apellidoP, String curp, String calle, String colonia, String numero,
                                     List<TelefonoDTO> telefonos) {
}


