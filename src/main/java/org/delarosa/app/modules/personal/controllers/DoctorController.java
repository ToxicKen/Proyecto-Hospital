package org.delarosa.app.modules.personal.controllers;


import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.personal.dtos.RegistroDoctorRequest;
import org.delarosa.app.modules.personal.services.DoctorService;
import org.delarosa.app.modules.security.dto.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registrar(@RequestBody RegistroDoctorRequest request) {
        return ResponseEntity.ok(doctorService.registrarDoctor(request));
    }

    @GetMapping("/doctor/ping")
    public ResponseEntity<String> doctorPing() {
        return ResponseEntity.ok("Pong");
    }
}
