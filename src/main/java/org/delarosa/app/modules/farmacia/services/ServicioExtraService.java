package org.delarosa.app.modules.farmacia.services;

import org.delarosa.app.modules.farmacia.dtos.MedicamentoEditRequest;
import org.delarosa.app.modules.farmacia.dtos.ServicioExtraEditRequest;
import org.delarosa.app.modules.farmacia.dtos.ServicioExtraResponse;
import org.delarosa.app.modules.farmacia.dtos.ServicioRegistroRequest;
import org.delarosa.app.modules.farmacia.entities.ServicioExtra;

import java.util.List;

public interface ServicioExtraService {
    ServicioExtraResponse crearServicioExtra(ServicioRegistroRequest dto);
    ServicioExtraResponse obtenerServicioById(Integer id);
    List<ServicioExtraResponse> consultarServicios();
    List<ServicioExtraResponse> consultarPorNombre(String nombre);
    ServicioExtraResponse editarServicio(Integer id, ServicioExtraEditRequest dto);
    void eliminarServicio(Integer id);

}
