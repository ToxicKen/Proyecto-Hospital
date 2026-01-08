package org.delarosa.app.modules.farmacia.controllers;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.farmacia.dtos.*;
import org.delarosa.app.modules.farmacia.entities.Ticket;
import org.delarosa.app.modules.farmacia.services.TicketService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recepcionista/venta")
@PreAuthorize("hasRole('RECEPCIONISTA')")
public class VentaController {

    private final TicketService ticketService;

    // ================= CREAR TICKET =================

    @PostMapping
    public ResponseEntity<TicketResponse> crearTicket(
            @RequestBody TicketRequest dto) {

        TicketResponse response = ticketService.createTicket(dto);
        return ResponseEntity.ok(response);
    }

    // ================= OBTENER TICKET =================

    @GetMapping("/{idTicket}")
    public ResponseEntity<TicketResponse> obtenerTicket(
            @PathVariable Integer idTicket) {

        Ticket ticket = ticketService.obtenerTicketById(idTicket);
        return ResponseEntity.ok(
                new TicketResponse(
                        ticket.getIdTicket(),
                        ticket.getFechaVenta(),
                        ticket.getEstatus().name(),
                        ticket.getTotal(),
                        null,
                        null
                )
        );
    }

    // ================= PAGAR TICKET =================

    @PostMapping("/pago")
    public ResponseEntity<?> pagarTicket(
            @RequestBody PagoResponse dto) {

        ticketService.pagarTicket(dto);
        return ResponseEntity.ok().body(
                java.util.Map.of("mensaje", "Ticket pagado correctamente")
        );
    }

    // ================= PDF TICKET =================

    @GetMapping("/{idTicket}/pdf")
    public ResponseEntity<byte[]> obtenerPDF(
            @PathVariable Integer idTicket) {

        byte[] pdf = ticketService.obtenerPDFTicket(idTicket);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=ticket_" + idTicket + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
