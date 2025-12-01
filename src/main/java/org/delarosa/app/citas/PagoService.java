package org.delarosa.app.citas;


import java.math.BigDecimal;

public interface PagoService{
    OrdenPago crearOrdenPago(Cita cita, BigDecimal montoCita);
}
