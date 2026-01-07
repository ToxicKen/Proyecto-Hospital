package org.delarosa.app.modules.farmacia.services;

import org.delarosa.app.modules.farmacia.dtos.MedicamentoRegistroRequest;
import org.delarosa.app.modules.farmacia.dtos.MedicamentoResponse;
import org.delarosa.app.modules.farmacia.entities.Medicamento;

import java.util.List;

public interface MedicamentoService {

    void crearMedicamento(MedicamentoRegistroRequest dto);
    Medicamento obtenerMedicamentoPorId(Integer id);
    void agregarStock(Integer id,Integer cantidad);
    void descontarStock(Integer id,Integer cantidad);
    void verificarStockParaVenta(Integer id,Integer cantidad);
    List<MedicamentoResponse> consultarMedicamentos();

}
