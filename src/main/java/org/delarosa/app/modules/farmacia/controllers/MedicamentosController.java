package org.delarosa.app.modules.farmacia.controllers;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.farmacia.dtos.MedicamentoResponse;
import org.delarosa.app.modules.farmacia.entities.Medicamento;
import org.delarosa.app.modules.farmacia.services.MedicamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/medicamentos")
@RequiredArgsConstructor
public class MedicamentosController {

    private final MedicamentoService medicamentoService;

    @GetMapping
    public List<MedicamentoResponse> consultarMedicamentos() {
        return medicamentoService.consultarMedicamentos();
    }

    @GetMapping
    public List<MedicamentoResponse> consultarMedicamentoPorNombre(){

    }


}
