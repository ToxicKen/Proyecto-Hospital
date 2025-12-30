package org.delarosa.app.modules.general.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name ="MetodoPago")
@Data
public class MetodoPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMetodoPago;
    
    private String Tipo;
}
