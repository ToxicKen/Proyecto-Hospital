package org.delarosa.app.empleado.doctor;


import org.delarosa.app.modules.security.dto.AuthResponse;

public interface DoctorService {
    AuthResponse registrarDoctor(DoctorDTO doctorDTO);
    Doctor crearDoctor(DoctorDTO doctorDTO);
    Doctor obtenerDoctorById(Integer id);

}
