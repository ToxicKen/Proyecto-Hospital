package org.delarosa.app.modules.clinico.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
@AllArgsConstructor
public class BitacoraResumenDTO {
    private Integer idHistorial;
    private LocalDate fechaCita;
    private LocalTime horaCita;
    private String nombreDoctor;
    private String especialidad;
    private String nombrePaciente;
    private String consultorio;



}
