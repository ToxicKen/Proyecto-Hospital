package org.delarosa.app.modules.farmacia.services;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.farmacia.dtos.MedicamentoEditRequest;
import org.delarosa.app.modules.farmacia.dtos.ServicioExtraEditRequest;
import org.delarosa.app.modules.farmacia.dtos.ServicioExtraResponse;
import org.delarosa.app.modules.farmacia.dtos.ServicioRegistroRequest;
import org.delarosa.app.modules.farmacia.entities.ServicioExtra;
import org.delarosa.app.modules.farmacia.repositories.ServicioExtraRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicioExtraServiceImp implements ServicioExtraService {

    private final ServicioExtraRepository servicioRepo;

    @Override
    public ServicioExtraResponse crearServicioExtra(ServicioRegistroRequest dto) {
        ServicioExtra servicio = ServicioExtra.builder().
                nombre(dto.nombre())
                .costo(dto.precio()).
                build();
        return mapearServicioAResponse(servicioRepo.save(servicio));
    }

    @Override
    public ServicioExtraResponse obtenerServicioById(Integer id) {
        return mapearServicioAResponse(obtenerServicioPorId(id));
    }

    @Override
    public List<ServicioExtraResponse> consultarServicios() {
        return servicioRepo.findAll().stream().map(this::mapearServicioAResponse).toList();
    }

    @Override
    public List<ServicioExtraResponse> consultarPorNombre(String nombre) {
        return servicioRepo.findByNombreContainingIgnoreCase(nombre).stream().map(this::mapearServicioAResponse).toList();
    }

    @Override
    public ServicioExtraResponse editarServicio(Integer id, ServicioExtraEditRequest dto) {
        ServicioExtra servicio = obtenerServicioPorId(id);
        servicio.setNombre(dto.nombre());
        servicio.setDescripcion(dto.descripcion());
        servicio.setActivo(dto.activo());
        return mapearServicioAResponse(servicioRepo.save(servicio));
    }

    @Override
    public void eliminarServicio(Integer id) {

    }

    private ServicioExtraResponse mapearServicioAResponse(ServicioExtra servicio) {
        return new ServicioExtraResponse(servicio.getIdServicioExtra(),servicio.getNombre(),servicio.getDescripcion(),servicio.getCosto(),servicio.getActivo());
    }

    private ServicioExtra obtenerServicioPorId(Integer id) {
        Optional<ServicioExtra> servicio = servicioRepo.findById(id);
        return servicio.orElseThrow(()->new RuntimeException("Servicio no encontrado"));
    }




}
