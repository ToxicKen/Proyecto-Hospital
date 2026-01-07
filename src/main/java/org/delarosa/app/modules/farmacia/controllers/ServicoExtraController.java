package org.delarosa.app.modules.farmacia.controllers;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.farmacia.dtos.ServicioExtraEditRequest;
import org.delarosa.app.modules.farmacia.dtos.ServicioExtraResponse;
import org.delarosa.app.modules.farmacia.dtos.ServicioRegistroRequest;
import org.delarosa.app.modules.farmacia.services.ServicioExtraService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recepcionista/servicioExtra")
@RequiredArgsConstructor
public class ServicoExtraController {

    private final ServicioExtraService servicioExtraService;

    @PostMapping("/crear")
    public ServicioExtraResponse crearServicioExtra(@RequestBody ServicioRegistroRequest dto) {
        return servicioExtraService.crearServicioExtra(dto);
    }

    @GetMapping("/consultar")
    public List<ServicioExtraResponse> consultarServicios() {
        return servicioExtraService.consultarServicios();
    }

    @GetMapping("/buscar/{nombre}")
    public List<ServicioExtraResponse> buscarServicio(@RequestParam String nombre) {
        return servicioExtraService.consultarPorNombre(nombre);
    }

    @GetMapping("/obtener/{idServicio}")
    public ServicioExtraResponse obtenerServicioExtra(@PathVariable Integer idServicio) {
        return servicioExtraService.obtenerServicioById(idServicio);
    }

    @PostMapping("/editar/{idServicio}")
    public ServicioExtraResponse editarServicioExtra(@PathVariable Integer idServicio,@RequestBody ServicioExtraEditRequest dto) {
        return servicioExtraService.editarServicio(idServicio,dto);
    }

//    @DeleteMapping("/eliminar/{idServicio}")
//    public ServicioExtraResponse eliminarServicioExtra(@PathVariable Integer idServicio) {
//        return servicioExtraService.eliminarServicio();
//    }

}
