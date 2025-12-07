package org.delarosa.app.modules.clinico.services;


import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.delarosa.app.modules.clinico.dtos.LineaPagoResponse;
import org.delarosa.app.modules.clinico.entities.Cita;
import org.delarosa.app.modules.clinico.entities.LineaPago;
import org.delarosa.app.modules.clinico.entities.OrdenPago;
import org.delarosa.app.modules.clinico.enums.EstatusCita;
import org.delarosa.app.modules.clinico.repositories.CitaRepository;
import org.delarosa.app.modules.clinico.repositories.LineaPagoRepository;
import org.delarosa.app.modules.clinico.repositories.OrdenPagoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PagoServiceImp implements PagoService {
    private final OrdenPagoRepository ordenPagoRepo;
    private final LineaPagoRepository lineaPagoRepo;
    private final CitaRepository citaRepo;

    @Override
    public OrdenPago crearOrdenPago(Cita cita, BigDecimal montoCita) {
        return OrdenPago.builder()
                .estatus(false)
                .montoTotal(montoCita)
                .build();
    }


    @Override
    public LineaPagoResponse pagarLineaPago(Integer idOrdenPago, BigDecimal monto) {
        OrdenPago orden = obtenerOrdenPago(idOrdenPago);
        LineaPago linea = crearLineaPago(orden, monto);
        actualizarEstatusOrden(orden);

        if (orden.getEstatus() && orden.getCita() != null) {
            Cita cita = orden.getCita();
            cita.setEstatus(EstatusCita.PAGADA_PENDIENTE_POR_ATENDER);
            citaRepo.save(cita);
        }

        BigDecimal totalPagado = lineaPagoRepo.sumarPagosPorOrden(orden.getIdOrdenPago());
        return new LineaPagoResponse(
                linea.getIdLineaPago(),
                linea.getFechaPago(),
                linea.getMontoPago(),
                orden.getEstatus(),
                orden.getIdOrdenPago(),
                orden.getMontoTotal(),
                totalPagado
        );
    }

    private OrdenPago obtenerOrdenPago(Integer idOrdenPago) {
        return ordenPagoRepo.findById(idOrdenPago)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    }


    private LineaPago crearLineaPago(OrdenPago ordenPago, BigDecimal montoLinea) {
        LineaPago lineaPago = LineaPago.builder()
                .ordenPago(ordenPago)
                .fechaPago(LocalDateTime.now())
                .montoPago(montoLinea)
                .build();
        return lineaPagoRepo.save(lineaPago);
    }

    private void actualizarEstatusOrden(OrdenPago orden) {
        BigDecimal totalPagado = lineaPagoRepo.sumarPagosPorOrden(orden.getIdOrdenPago());
        orden.setEstatus(totalPagado.compareTo(orden.getMontoTotal()) >= 0);
        ordenPagoRepo.save(orden);
    }






}
