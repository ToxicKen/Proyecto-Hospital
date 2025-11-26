package org.delarosa.app.empleado.doctor;


import lombok.RequiredArgsConstructor;
import org.delarosa.app.paciente.PacienteDTO;
import org.delarosa.app.security.auth.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @PostMapping("/auth/doctor/registro")
    public ResponseEntity<AuthResponse> registrar(@RequestBody DoctorDTO request) {
        return ResponseEntity.ok(doctorService.registrarDoctor(request));
    }

    @GetMapping("/doctor/ping")
    public ResponseEntity<String> doctorPing() {
        return ResponseEntity.ok("Pong");
    }
}
