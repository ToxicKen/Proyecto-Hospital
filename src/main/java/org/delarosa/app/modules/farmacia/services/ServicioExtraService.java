package org.delarosa.app.modules.farmacia.services;

import org.delarosa.app.modules.farmacia.dtos.ServicioRegistroRequest;
import org.delarosa.app.modules.farmacia.entities.ServicioExtra;

public interface ServicioExtraService {
    void crearServicioExtra(ServicioRegistroRequest dto);
    ServicioExtra obtenerServicioById(Integer id);
//    void agregarStock(Integer id,Integer cantidad);
//    void descontarStock(Integer id,Integer cantidad);
//    void verificarStockParaVenta(Integer id,Integer cantidad);


}
