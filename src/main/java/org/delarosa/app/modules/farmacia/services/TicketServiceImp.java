package org.delarosa.app.modules.farmacia.services;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.farmacia.dtos.*;
import org.delarosa.app.modules.farmacia.entities.*;
import org.delarosa.app.modules.farmacia.enums.EstatusTicket;
import org.delarosa.app.modules.farmacia.repositories.MetodoPagoRepository;
import org.delarosa.app.modules.farmacia.repositories.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImp implements TicketService {
    private final MedicamentoService medicamentoService;
    private final ServicioExtraService servicioExtraService;
    private final TicketRepository ticketRepo;
    private final MetodoPagoRepository metodoPagoRepo;

    @Override
    @Transactional
    public TicketResponse createTicket(TicketRequest dto) {
        Ticket ticket = createTicketEntity(dto);
        ticketRepo.save(ticket);
        return mapearAResponseTicket(ticket);
    }

    @Override
    public Ticket obtenerTicketById(Integer id) {
        return ticketRepo.findById(id).orElseThrow(()-> new RuntimeException("Ticket no encontrado"));
    }


    @Override
    public byte[] obtenerPDFTicket(Integer idTicket) {
        return new byte[0];
    }

    @Override
    @Transactional
    public void pagarTicket(PagoResponse dto) {
        Ticket ticket = obtenerTicketById(dto.idTicket());
        if (ticket.getEstatus() != EstatusTicket.PENDIENTE) {
            throw new RuntimeException("El ticket ya fue pagado o cancelado");
        }
        if (dto.montoPago().compareTo(ticket.getTotal()) < 0) {
            throw new RuntimeException("Monto insuficiente");
        }

        PagoTicket pago = new PagoTicket();
        pago.setTicket(ticket);
        pago.setMetodoPago(metodoPagoRepo.findById(dto.idMetodoPago()).orElseThrow(()-> new RuntimeException("Metodo pago no encontrado")));

    }

    private Ticket createTicketEntity(TicketRequest dto) {
        Ticket ticket = new Ticket();
        agregarMedicamentosTicket(ticket, dto.medicamentos());
        agregarServiciosTicket(ticket, dto.servicios());
        guardarTotalTicket(ticket);
        return ticket;
    }

    private void agregarMedicamentosTicket(Ticket ticket, List<MedicamentoTicketRequest> dto) {
        for (MedicamentoTicketRequest m : dto) {
            Medicamento medicamento = medicamentoService.obtenerMedicamentoPorId(m.idMedicamento());
            DetalleMedicamento detalle = new DetalleMedicamento();
            detalle.setMedicamento(medicamento);
            detalle.setCantidad(m.cantidad());
            detalle.setPrecioUnitario(medicamento.getPrecio());
            detalle.setSubtotal(medicamento.getPrecio().multiply(BigDecimal.valueOf(m.cantidad())));
            ticket.agregarMedicamento(detalle);
        }

    }

    private void agregarServiciosTicket(Ticket ticket, List<ServicioTicketRequest> servicios) {
        for (ServicioTicketRequest s : servicios) {
            ServicioExtra servicio = servicioExtraService.obtenerServicioById(s.idServicio());
            DetalleServicioExtra detalle = new DetalleServicioExtra();
            detalle.setServicioExtra(servicio);
            detalle.setCantidad(s.cantidad());
            detalle.setPrecioUnitario(servicio.getCosto());
            detalle.setSubtotal(detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())));
            ticket.agregarServicio(detalle);
        }
    }

    private void guardarTotalTicket(Ticket ticket) {
        BigDecimal total = new BigDecimal("0.00");
        for (DetalleMedicamento m :ticket.getMedicamentos()){
            total = total.add(m.getSubtotal());
        }
        for (DetalleServicioExtra s :ticket.getServicios()){
            total = total.add(s.getSubtotal());
        }
        ticket.setTotal(total);
    }

    private TicketResponse mapearAResponseTicket(Ticket ticket) {
        return new TicketResponse(ticket.getIdTicket(),ticket.getFechaVenta(),ticket.getEstatus().name(),ticket.getTotal(),
                mapearAResponseMedicamentos(ticket.getMedicamentos()),mapearAResponseServicios(ticket.getServicios()));
    }

    private List<DetalleMedicamentoResponse> mapearAResponseMedicamentos(List<DetalleMedicamento> medicamentos){
        return medicamentos.stream().map(m-> new DetalleMedicamentoResponse(m.getMedicamento().getIdMedicamento(),m.getMedicamento().getNombre(),m.getCantidad(),m.getPrecioUnitario(),m.getSubtotal())).toList();
    }


    private List<DetalleServicioExtraResponse> mapearAResponseServicios(List<DetalleServicioExtra> servicios){
        return servicios.stream().map(m-> new DetalleServicioExtraResponse(m.getServicioExtra().getIdServicioExtra(),m.getServicioExtra().getNombre(),m.getCantidad(),m.getPrecioUnitario(),m.getSubtotal())).toList();
    }


}
