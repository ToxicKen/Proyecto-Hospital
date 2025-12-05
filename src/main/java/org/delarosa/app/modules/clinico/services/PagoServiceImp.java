package org.delarosa.app.modules.clinico.services;


import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.clinico.entities.Cita;
import org.delarosa.app.modules.clinico.entities.OrdenPago;
import org.delarosa.app.modules.clinico.repositories.OrdenPagoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PagoServiceImp implements PagoService {
    private final OrdenPagoRepository ordenPagoRepo;

    public OrdenPago crearOrdenPago(Cita cita, BigDecimal montoCita) {
        return OrdenPago.builder()
                .estatus(false)
                .montoTotal(montoCita)
                .build();
    }


}
