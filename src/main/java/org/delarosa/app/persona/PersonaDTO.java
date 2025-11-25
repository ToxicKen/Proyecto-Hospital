package org.delarosa.app.persona;

import java.util.List;

public record PersonaDTO(String nombre, String apellidoM, String apellidoP,String curp, String calle, String colonia, String numero,
                         List<TelefonoDTO> telefonos) {
}


