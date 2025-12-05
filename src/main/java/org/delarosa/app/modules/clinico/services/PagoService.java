package org.delarosa.app.modules.clinico.services;


import org.delarosa.app.modules.clinico.entities.Cita;
import org.delarosa.app.modules.clinico.entities.OrdenPago;

import java.math.BigDecimal;

public interface PagoService{
    OrdenPago crearOrdenPago(Cita cita, BigDecimal montoCita);
}
