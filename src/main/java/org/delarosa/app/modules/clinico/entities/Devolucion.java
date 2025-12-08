package org.delarosa.app.modules.clinico.entities;

import jakarta.persistence.*;
import lombok.*;
import org.delarosa.app.modules.clinico.enums.TipoDevolucion;

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

    private Integer porcentajeDevolucion;

}
