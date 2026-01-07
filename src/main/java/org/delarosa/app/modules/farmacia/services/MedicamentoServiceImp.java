package org.delarosa.app.modules.farmacia.services;


import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.farmacia.dtos.MedicamentoEditRequest;
import org.delarosa.app.modules.farmacia.dtos.MedicamentoRegistroRequest;
import org.delarosa.app.modules.farmacia.dtos.MedicamentoResponse;
import org.delarosa.app.modules.farmacia.entities.Medicamento;
import org.delarosa.app.modules.farmacia.entities.MovimientoStock;
import org.delarosa.app.modules.farmacia.enums.TipoMovimiento;
import org.delarosa.app.modules.farmacia.repositories.MedicamentoRepository;
import org.delarosa.app.modules.farmacia.repositories.MovimientoStockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MedicamentoServiceImp implements MedicamentoService {

    private final MedicamentoRepository medicamentoRepo;
    private final MovimientoStockRepository movimientoStockRepo;

    @Transactional
    @Override
    public void agregarStock(Integer id, Integer cantidad) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }

        Medicamento medicamento = obtenerMedicamentoPorId(id);

        int stockAnterior = medicamento.getStock();
        int stockFinal = stockAnterior + cantidad;

        medicamento.setStock(stockFinal);

        movimientoStockRepo.save(
                MovimientoStock.builder()
                        .medicamento(medicamento)
                        .cantidad(cantidad)
                        .tipo(TipoMovimiento.ENTRADA)
                        .stockAnterior(stockAnterior)
                        .stockFinal(stockFinal)
                        .motivo("Entrada de stock")
                        .build()
        );
    }


    @Transactional
    @Override
    public void descontarStock(Integer id, Integer cantidad) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }

        Medicamento medicamento = obtenerMedicamentoPorId(id);

        verificarStockSuficiente(medicamento, cantidad);

        int stockAnterior = medicamento.getStock();
        int stockFinal = stockAnterior - cantidad;

        medicamento.setStock(stockFinal);

        movimientoStockRepo.save(
                MovimientoStock.builder()
                        .medicamento(medicamento)
                        .cantidad(-cantidad)
                        .tipo(TipoMovimiento.SALIDA)
                        .stockAnterior(stockAnterior)
                        .stockFinal(stockFinal)
                        .motivo("Salida de stock")
                        .build()
        );
    }

    @Transactional
    @Override
    public MedicamentoResponse crearMedicamento(MedicamentoRegistroRequest dto) {

        if (medicamentoRepo.findByNombre(dto.nombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe el medicamento");
        }

        Medicamento medicamento = Medicamento.builder()
                .nombre(dto.nombre())
                .stock(dto.cantidad())
                .precio(dto.precio())
                .descripcion(dto.descripcion())
                .build();

        Medicamento guardado = medicamentoRepo.save(medicamento);

        movimientoStockRepo.save(
                MovimientoStock.builder()
                        .medicamento(guardado)
                        .cantidad(dto.cantidad())
                        .tipo(TipoMovimiento.ENTRADA)
                        .stockAnterior(0)
                        .stockFinal(dto.cantidad())
                        .motivo("Stock inicial")
                        .build()
        );

        return mapearAResponse(guardado);
    }

    @Override
    public MedicamentoResponse obtenerMedicamentoById(Integer id) {
        return mapearAResponse(obtenerMedicamentoPorId(id));
    }

    private Medicamento obtenerMedicamentoPorId(Integer idMedicamento) {
        return medicamentoRepo.findById(idMedicamento)
                .orElseThrow(() -> new RuntimeException("Medicamento no encontrado"));
    }

    private void verificarStockSuficiente(Medicamento medicamento, int cantidad) {
        if (medicamento.getStock() < cantidad) {
            throw new IllegalStateException("Stock insuficiente");
        }
    }

    @Override
    public List<MedicamentoResponse> consultarMedicamentos() {
        return medicamentoRepo.findAll().stream().map(this::mapearAResponse).toList();
    }

    @Override
    public List<MedicamentoResponse> buscarPorNombre(String nombre) {
        return medicamentoRepo.findByNombreContainingIgnoreCase(nombre).stream().map(this::mapearAResponse).toList();
    }

    @Override
    public void editarMedicamento(Integer id, MedicamentoEditRequest dto) {
        Medicamento medicamento = obtenerMedicamentoPorId(id);
        medicamento.setNombre(dto.nombre());
        medicamento.setDescripcion(dto.descripcion());
        medicamento.setPrecio(dto.precio());
        medicamentoRepo.save(medicamento);
    }

    @Override
    public void eliminarMedicamento(Integer id) {

    }

    private MedicamentoResponse mapearAResponse(Medicamento medicamento) {
        return new MedicamentoResponse(medicamento.getIdMedicamento(), medicamento.getNombre(), medicamento.getDescripcion(), medicamento.getPrecio(), medicamento.getStock());
    }

}
