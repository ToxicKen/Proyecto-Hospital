package org.delarosa.app.empleado.recepcionista;

import org.delarosa.app.empleado.EmpleadoDTO;
import org.delarosa.app.empleado.doctor.Doctor;
import org.delarosa.app.empleado.doctor.DoctorDTO;
import org.delarosa.app.security.auth.AuthResponse;

public interface RecepcionistaService {
    AuthResponse registrarRecepcionista(EmpleadoDTO empleadoDTO);
    Recepcionista crearRecepcionista(EmpleadoDTO empleadoDTO);
}
