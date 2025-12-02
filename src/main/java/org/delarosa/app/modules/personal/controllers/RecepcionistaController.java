package org.delarosa.app.modules.personal.controllers;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.personal.services.RecepcionistaService;
import org.delarosa.app.modules.personal.dtos.RegistroEmpleadoRequest;
import org.delarosa.app.modules.security.dto.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecepcionistaController {

    private final RecepcionistaService recepcionistaService;

    @PostMapping("/auth/recepcionista/registro")
    public ResponseEntity<AuthResponse> registrarRecepcionista(@RequestBody RegistroEmpleadoRequest registroEmpleadoRequest) {
        return ResponseEntity.ok(recepcionistaService.registrarRecepcionista(registroEmpleadoRequest));
    }

}
