package org.delarosa.app.modules.clinico.services;


import org.delarosa.app.modules.clinico.dtos.*;
import org.delarosa.app.modules.clinico.entities.Cita;
import org.delarosa.app.modules.clinico.enums.EstatusCita;
import org.delarosa.app.modules.paciente.entities.Paciente;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CitaService {
    CitaResponse crearCita(CrearCitaRequest crearCitaRequest, Paciente paciente);

    Cita obtenerById(Integer id);

    List<LocalTime> obtenerHorasDisponiblesByDoctorIdYFecha(Integer idDoctor,LocalDate dia);


    List<CitaResponse> obtenerCitasPacientePorEstatus(Integer idPaciente, EstatusCita estatus);

    List<CitaResponse> obtenerCitasPacientePorFechas(Integer idPaciente, LocalDate fechaInicio, LocalDate fechaFin);

    List<CitaResponse> obtenerCitasPacientePorDoctor(Integer idPaciente, String nombre);

    List<CitaResponse> obtenerCitasPaciente(Integer idPaciente);

    List<CitaResponse> obtenerCitasDoctor(Integer idDoctor);

    Integer CrearReceta(RecetaRequest recetaRequest);

    List<RecetaPDF> obtenerRecetasPorDoctor(Integer idDoctor);

    String marcarPacienteNoAcudio(Integer folioCita);

    Integer cancelarCitaPorPaciente(Integer idCita,Integer idPaciente);



}
