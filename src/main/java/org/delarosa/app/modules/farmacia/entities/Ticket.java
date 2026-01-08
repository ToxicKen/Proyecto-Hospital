package org.delarosa.app.modules.farmacia.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.delarosa.app.modules.farmacia.enums.EstatusTicket;
import org.delarosa.app.modules.personal.entities.Recepcionista;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTicket;

    @ManyToOne
    @JoinColumn(name = "idRecepcionista", referencedColumnName = "idRecepcionista",nullable = false)
    private Recepcionista recepcionista;

    @Column(nullable = false)
    private LocalDateTime fechaVenta;

    @Column(nullable = false)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstatusTicket estatus;

    @OneToOne(mappedBy = "ticket",orphanRemoval = true,cascade = CascadeType.ALL)
    private PagoTicket pago;

    @OneToMany(mappedBy = "ticket",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<DetalleMedicamento> medicamentos = new ArrayList<>();

    @OneToMany(mappedBy = "ticket",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<DetalleServicioExtra> servicios = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.fechaVenta = LocalDateTime.now();
        this.estatus = EstatusTicket.PENDIENTE;
    }

    public void agregarMedicamento(DetalleMedicamento detalle) {
        detalle.setTicket(this);
        this.medicamentos.add(detalle);
    }
    public void agregarServicio(DetalleServicioExtra servicioExtra) {
        this.servicios.add(servicioExtra);
        servicioExtra.setTicket(this);
    }
}
