package org.delarosa.app.modules.general.services;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.general.entities.Telefono;
import org.delarosa.app.modules.general.repositories.TelefonoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TelefonoServiceImp implements TelefonoService {

    private final TelefonoRepository telefonoRepo;


    @Transactional
    public Telefono buscarOCrearTelefono(String numero) {
        // Validar que el número no sea nulo o vacío
        if (numero == null || numero.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de teléfono no puede estar vacío");
        }

        String numeroLimpio = numero.trim();

        // Buscar el teléfono existente
        Telefono telefonoExistente = telefonoRepo.findByNumeroTelefono(numeroLimpio);

        if (telefonoExistente != null) {
            return telefonoExistente;
        }
        // Si no existe, crear uno nuevo
        Telefono nuevoTelefono = new Telefono();
        nuevoTelefono.setNumeroTelefono(numeroLimpio);
        return telefonoRepo.save(nuevoTelefono);
    }
}
