package org.delarosa.app.modules.farmacia.services;


import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.farmacia.dtos.MedicamentoRegistroRequest;
import org.delarosa.app.modules.farmacia.entities.Medicamento;
import org.delarosa.app.modules.farmacia.repositories.MedicamentoRepository;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MedicamentoServiceImp implements MedicamentoService {

    private final MedicamentoRepository medicamentoRepo;

    @Override
    public void agregarStock(Integer id, Integer cantidad) {
        Medicamento medicamento = obtenerMedicamentoPorId(id);

        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }

        medicamento.setStock(medicamento.getStock() + cantidad);

        medicamentoRepo.save(medicamento);
    }

    @Override
    public void descontarStock(Integer id, Integer cantidad) {
        Medicamento medicamento = obtenerMedicamentoPorId(id);
        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }
        verificarStockParaVenta(medicamento.getStock(), cantidad);
        medicamento.setStock(medicamento.getStock() - cantidad);
        medicamentoRepo.save(medicamento);
    }

    @Override
    public void crearMedicamento(MedicamentoRegistroRequest dto) {

        if (medicamentoRepo.findByNombre(dto.nombre()).isPresent()) {
            throw new RuntimeException("Ya existe el medicamento");
        }

        Medicamento medicamento = Medicamento.builder()
                .nombre(dto.nombre())
                .stock(dto.cantidad())
                .precio(dto.precio())
                .build();

        medicamentoRepo.save(medicamento);
    }

    @Override
    public Medicamento obtenerMedicamentoPorId(Integer idMedicamento) {
        return medicamentoRepo.findById(idMedicamento)
                .orElseThrow(() -> new RuntimeException("Medicamento no encontrado"));
    }

    @Override
    public void verificarStockParaVenta(Integer idMedicamento, Integer cantidad) {
        Medicamento medicamento = obtenerMedicamentoPorId(idMedicamento);

        if (medicamento.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }
    }


}
