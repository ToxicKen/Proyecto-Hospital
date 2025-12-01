package org.delarosa.app.empleado.doctor;


import org.delarosa.app.paciente.Paciente;
import org.delarosa.app.paciente.PacienteDTO;
import org.delarosa.app.security.auth.AuthResponse;

public interface DoctorService {
    AuthResponse registrarDoctor(DoctorDTO doctorDTO);
    Doctor crearDoctor(DoctorDTO doctorDTO);
    Doctor obtenerDoctorById(Integer id);

}
