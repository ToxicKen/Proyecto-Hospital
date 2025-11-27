package org.delarosa.app.empleado.recepcionista;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.empleado.EmpleadoDTO;
import org.delarosa.app.security.auth.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecepcionistaController {
    private final RecepcionistaService recepcionistaService;

    @PostMapping("/auth/recepcionista/registro")
    public ResponseEntity<AuthResponse> registrarRecepcionista(@RequestBody EmpleadoDTO empleadoDTO) {
        return ResponseEntity.ok(recepcionistaService.registrarRecepcionista(empleadoDTO));
    }

}
