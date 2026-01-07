package org.delarosa.app.modules.farmacia.services;

import org.delarosa.app.modules.farmacia.dtos.MedicamentoEditRequest;
import org.delarosa.app.modules.farmacia.dtos.MedicamentoRegistroRequest;
import org.delarosa.app.modules.farmacia.dtos.MedicamentoResponse;
import org.delarosa.app.modules.farmacia.entities.Medicamento;

import java.util.List;

public interface MedicamentoService {

    MedicamentoResponse crearMedicamento(MedicamentoRegistroRequest dto);
    MedicamentoResponse obtenerMedicamentoById(Integer id);
    void agregarStock(Integer id,Integer cantidad);
    void descontarStock(Integer id,Integer cantidad);
    List<MedicamentoResponse> consultarMedicamentos();
    List<MedicamentoResponse> buscarPorNombre(String nombre);
    void editarMedicamento(Integer id, MedicamentoEditRequest dto);
    void eliminarMedicamento(Integer id);


}
