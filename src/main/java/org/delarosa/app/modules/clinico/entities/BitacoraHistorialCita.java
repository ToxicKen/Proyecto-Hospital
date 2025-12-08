package org.delarosa.app.modules.clinico.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "Bitacora_Historial_Citas")
public class BitacoraHistorialCita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Historial")
    private Integer idHistorial;

    @Column(name = "Usuario")
    private String usuario;

    @Column(name = "Maquina_IP")
    private String maquinaIp;

    @Column(name = "Folio_cita")
    private Integer folioCita;

    @Column(name = "Fecha_cita")
    private LocalDate fechaCita;

    @Column(name = "Hora_cita")
    private LocalTime horaCita;

    @Column(name = "Id_Paciente")
    private Integer idPaciente;

    @Column(name = "Folio_receta")
    private Integer folioReceta;

    @Column(name = "Id_Doctor")
    private Integer idDoctor;

    @Column(name = "Estatus_consulta")
    private String estatusConsulta;

    @Column(name = "Especialidad")
    private String especialidad;

    @Column(name = "Consultorio")
    private Integer consultorio;
}