package org.delarosa.app.citas;

import jakarta.persistence.*;
import lombok.*;
import org.delarosa.app.paciente.Paciente;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Devolucion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDevolucion;

    @OneToOne
    @JoinColumn(name = "idOrdenPago",referencedColumnName = "idOrdenPago")
    private OrdenPago ordenPago;

    private LocalDateTime fechaDevolucion;

    @Enumerated(EnumType.STRING)
    private TipoDevolucion tipoDevolucion;

    private Integer PorcentajeDevuelto;



}
