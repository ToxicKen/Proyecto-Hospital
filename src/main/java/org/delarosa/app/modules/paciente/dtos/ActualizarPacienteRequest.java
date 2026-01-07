package org.delarosa.app.modules.paciente.dtos;

import org.delarosa.app.modules.general.dtos.TelefonoDTO;

import java.util.List;

public record ActualizarPacienteRequest(String nombre,
                                        String apellidoP,
                                        String apellidoM,
                                        String curp,
                                        String calle,
                                        String colonia,
                                        String numero,
                                        List<TelefonoDTO> telefonos,
                                        String email,
                                        Double peso,
                                        Double estatura,
                                        String tipoSangre,
                                        List<String> nuevasAlergias,
                                        List<Integer> idAlergias,
                                        List<PadecimientoDTO> padecimientos) {
}
