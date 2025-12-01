package org.delarosa.app.citas;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PagoServiceImp implements PagoService{
    private final OrdenPagoRepository ordenPagoRepo;

    public OrdenPago crearOrdenPago(Cita cita, BigDecimal montoCita) {
        return OrdenPago.builder()
                .estatus(false)
                .montoTotal(montoCita)
                .build();
    }


}
