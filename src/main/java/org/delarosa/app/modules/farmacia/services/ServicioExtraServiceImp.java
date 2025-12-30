package org.delarosa.app.modules.farmacia.services;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.farmacia.dtos.ServicioRegistroRequest;
import org.delarosa.app.modules.farmacia.entities.ServicioExtra;
import org.delarosa.app.modules.farmacia.repositories.ServicioExtraRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicioExtraServiceImp implements ServicioExtraService {

    private final ServicioExtraRepository servicioRepo;

    @Override
    public void crearServicioExtra(ServicioRegistroRequest dto) {
        ServicioExtra servicio = ServicioExtra.builder().
                nombre(dto.nombre())
                .costo(dto.precio()).
                build();
        servicioRepo.save(servicio);
    }

    @Override
    public ServicioExtra obtenerServicioById(Integer id) {
        Optional<ServicioExtra> servicio = servicioRepo.findById(id);
        return servicio.orElseThrow(()->new RuntimeException("Servicio no encontrado"));
    }
}
