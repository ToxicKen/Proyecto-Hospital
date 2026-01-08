package org.delarosa.app.modules.farmacia.controllers;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.farmacia.dtos.MedicamentoEditRequest;
import org.delarosa.app.modules.farmacia.dtos.MedicamentoRegistroRequest;
import org.delarosa.app.modules.farmacia.dtos.MedicamentoResponse;
import org.delarosa.app.modules.farmacia.entities.Medicamento;
import org.delarosa.app.modules.farmacia.services.MedicamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recepcionista/medicamentos")
@RequiredArgsConstructor
public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    @GetMapping("/consultar")
    public List<MedicamentoResponse> consultarMedicamentos() {
        return medicamentoService.consultarMedicamentos();
    }

    @GetMapping("/buscar")
    public List<MedicamentoResponse> buscarPorNombre(
            @RequestParam String nombre
    ) {
        return medicamentoService.buscarPorNombre(nombre);
    }

    @PostMapping("/crear")
    public ResponseEntity<MedicamentoResponse> crearMedicamento(
            @RequestBody MedicamentoRegistroRequest dto
    ) {
        MedicamentoResponse response = medicamentoService.crearMedicamento(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/editar/{idMedicamento}")
    public ResponseEntity<Void> editarMedicamento(@PathVariable Integer idMedicamento,@RequestBody MedicamentoEditRequest dto) {
        medicamentoService.editarMedicamento(idMedicamento, dto);
        return ResponseEntity.noContent().build();
    };

    @GetMapping("/obtener/{idMedicamento}")
    public MedicamentoResponse obtenerMedicamento(@PathVariable Integer idMedicamento) {
        return medicamentoService.obtenerMedicamentoById(idMedicamento);
    }



}
