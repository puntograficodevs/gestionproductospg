package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.RotulacionDTO;
import com.puntografico.puntografico.repository.OrdenTrabajoRepository;
import com.puntografico.puntografico.repository.RotulacionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.transaction.Transactional;
import java.util.Optional;

@Service @Transactional @AllArgsConstructor
public class RotulacionService {

    private final RotulacionRepository rotulacionRepository;
    private final OpcionesRotulacionService opcionesRotulacionService;
    private final OrdenTrabajoRepository ordenTrabajoRepository;

    public Rotulacion guardar(RotulacionDTO rotulacionDTO, Long idOrdenTrabajo) {
        validarRotulacionDTO(rotulacionDTO);
        Rotulacion rotulacion = devolverRotulacionCorrespondiente(idOrdenTrabajo);

        TipoRotulacion tipoRotulacion = opcionesRotulacionService.buscarTipoRotulacionPorId(rotulacionDTO.getTipoRotulacionId());
        TipoCorteRotulacion tipoCorteRotulacion = opcionesRotulacionService.buscarTipoCorteRotulacionPorId(rotulacionDTO.getTipoCorteRotulacionId());

        boolean esLaminado = rotulacionDTO.getEsLaminado();
        boolean adicionalDisenio = rotulacionDTO.getConAdicionalDisenio();

        rotulacion.setEsLaminado(esLaminado);
        rotulacion.setHorarioRotulacion(rotulacionDTO.getHorarioRotulacion());
        rotulacion.setDireccionRotulacion(rotulacionDTO.getDireccionRotulacion());
        rotulacion.setMedida(rotulacionDTO.getMedida());
        rotulacion.setEnlaceArchivo(rotulacionDTO.getEnlaceArchivo());
        rotulacion.setConAdicionalDisenio(adicionalDisenio);
        rotulacion.setInformacionAdicional(rotulacionDTO.getInformacionAdicional());
        rotulacion.setCantidad(rotulacionDTO.getCantidad());
        rotulacion.setTipoRotulacion(tipoRotulacion);
        rotulacion.setTipoCorteRotulacion(tipoCorteRotulacion);

        return rotulacionRepository.save(rotulacion);
    }

    private void validarRotulacionDTO(RotulacionDTO rotulacionDTO) {
        Assert.notNull(rotulacionDTO.getMedida(), "La medida es un dato obligatorio.");
        Assert.notNull(rotulacionDTO.getCantidad(), "La cantidad es un dato obligatorio.");
        Assert.notNull(rotulacionDTO.getHorarioRotulacion(), "El horario de rotulación es un dato obligatorio.");
        Assert.notNull(rotulacionDTO.getDireccionRotulacion(), "La dirección de rotulación es un dato obligatorio.");
        Assert.notNull(rotulacionDTO.getTipoCorteRotulacionId(), "El tipo de corte es un dato obligatorio.");
        Assert.notNull(rotulacionDTO.getTipoRotulacionId(), "El tipo de rotulación es un dato obligatorio.");
    }

    private Rotulacion devolverRotulacionCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    Rotulacion rotulacionNueva = new Rotulacion();
                    rotulacionNueva.setOrdenTrabajo(ordenTrabajo);
                    return rotulacionNueva;
                });
    }

    public Optional<Rotulacion> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return rotulacionRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        Rotulacion rotulacion = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        rotulacionRepository.deleteById(rotulacion.getId());
    }
}
