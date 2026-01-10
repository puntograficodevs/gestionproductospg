package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.SublimacionDTO;
import com.puntografico.puntografico.repository.OrdenTrabajoRepository;
import com.puntografico.puntografico.repository.SublimacionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

@Service @Transactional @AllArgsConstructor
public class SublimacionService {

    private final SublimacionRepository sublimacionRepository;
    private final OpcionesSublimacionService opcionesSublimacionService;
    private final OrdenTrabajoRepository ordenTrabajoRepository;

    public Sublimacion guardar(SublimacionDTO sublimacionDTO, Long idOrdenTrabajo) {
        validarSublimacionDTO(sublimacionDTO);
        Sublimacion sublimacion = devolverSublimacionCorrespondiente(idOrdenTrabajo);

        MaterialSublimacion materialSublimacion = opcionesSublimacionService.buscarMaterialSublimacionPorId(sublimacionDTO.getMaterialSublimacionId());
        CantidadSublimacion cantidadSublimacion = opcionesSublimacionService.buscarCantidadSublimacionPorId(sublimacionDTO.getCantidadSublimacionId());

        Integer cantidad = sublimacionDTO.getCantidad();

        if (cantidad == null || cantidad == 0 || cantidadSublimacion.getId() != 6) {
            cantidad = Integer.valueOf(cantidadSublimacion.getCantidad());
        }

        boolean adicionalDisenio = sublimacionDTO.getConAdicionalDisenio();

        sublimacion.setEnlaceArchivo(sublimacionDTO.getEnlaceArchivo());
        sublimacion.setConAdicionalDisenio(adicionalDisenio);
        sublimacion.setInformacionAdicional(sublimacionDTO.getInformacionAdicional());
        sublimacion.setMaterialSublimacion(materialSublimacion);
        sublimacion.setCantidadSublimacion(cantidadSublimacion);
        sublimacion.setCantidad(cantidad);

        return sublimacionRepository.save(sublimacion);
    }

    private void validarSublimacionDTO(SublimacionDTO sublimacionDTO) {
        Assert.notNull(sublimacionDTO.getMaterialSublimacionId(), "materialSublimacionString es un dato obligatorio.");
        Assert.notNull(sublimacionDTO.getCantidadSublimacionId(), "cantidadSublimacionString es un dato obligatorio.");
    }

    private Sublimacion devolverSublimacionCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    Sublimacion sublimacionNueva = new Sublimacion();
                    sublimacionNueva.setOrdenTrabajo(ordenTrabajo);
                    return sublimacionNueva;
                });
    }

    public Optional<Sublimacion> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return sublimacionRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        Sublimacion sublimacion = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        sublimacionRepository.deleteById(sublimacion.getId());
    }
}
