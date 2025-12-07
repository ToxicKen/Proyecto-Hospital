package org.delarosa.app.modules.clinico.services;


import org.delarosa.app.modules.clinico.dtos.LineaPagoResponse;
import org.delarosa.app.modules.clinico.entities.Cita;
import org.delarosa.app.modules.clinico.entities.LineaPago;
import org.delarosa.app.modules.clinico.entities.OrdenPago;

import java.math.BigDecimal;

public interface PagoService {
    OrdenPago crearOrdenPago(Cita cita, BigDecimal montoCita);
//
//    LineaPago crearLineaPago(OrdenPago ordenPago, BigDecimal montoLinea);
//
//    OrdenPago obtenerOrdenPago(Integer idOrdenPago);

    LineaPagoResponse pagarLineaPago(Integer idOrdenPago ,BigDecimal monto);

}
