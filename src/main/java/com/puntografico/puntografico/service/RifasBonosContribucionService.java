package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.RifasBonosContribucionDTO;
import com.puntografico.puntografico.repository.OrdenTrabajoRepository;
import com.puntografico.puntografico.repository.RifasBonosContribucionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

@Service @Transactional @AllArgsConstructor
public class RifasBonosContribucionService {

    private final RifasBonosContribucionRepository rifasBonosContribucionRepository;
    private final OpcionesRifasContribucionService opcionesRifasContribucionService;
    private final OrdenTrabajoRepository ordenTrabajoRepository;

    public RifasBonosContribucion guardar(RifasBonosContribucionDTO rifasBonosContribucionDTO, Long idOrdenTrabajo) {
        validarRifasBonosContribucionDTO(rifasBonosContribucionDTO);
        RifasBonosContribucion rifasBonosContribucion = devolverRifasBonosContribucionCorrespondiente(idOrdenTrabajo);

        TipoPapelRifa tipoPapelRifa = opcionesRifasContribucionService.buscarTipoPapelRifaPorId(rifasBonosContribucionDTO.getTipoPapelRifaId());
        TipoTroqueladoRifa tipoTroqueladoRifa = opcionesRifasContribucionService.buscarTipoTroqueladoRifaPorId(rifasBonosContribucionDTO.getTipoTroqueladoRifaId());
        TipoColorRifa tipoColorRifa = opcionesRifasContribucionService.buscarTipoColorRifaPorId(rifasBonosContribucionDTO.getTipoColorRifaId());

        boolean adicionalDisenio = rifasBonosContribucionDTO.getConAdicionalDisenio();
        boolean conNumerado = rifasBonosContribucionDTO.getConNumerado();
        boolean conEncolado = rifasBonosContribucionDTO.getConEncolado();

        rifasBonosContribucion.setConNumerado(conNumerado);
        rifasBonosContribucion.setDetalleNumerado(rifasBonosContribucionDTO.getDetalleNumerado());
        rifasBonosContribucion.setConEncolado(conEncolado);
        rifasBonosContribucion.setMedida(rifasBonosContribucionDTO.getMedida());
        rifasBonosContribucion.setEnlaceArchivo(rifasBonosContribucionDTO.getEnlaceArchivo());
        rifasBonosContribucion.setConAdicionalDisenio(adicionalDisenio);
        rifasBonosContribucion.setInformacionAdicional(rifasBonosContribucionDTO.getInformacionAdicional());
        rifasBonosContribucion.setCantidad(rifasBonosContribucionDTO.getCantidad());
        rifasBonosContribucion.setTipoPapelRifa(tipoPapelRifa);
        rifasBonosContribucion.setTipoTroqueladoRifa(tipoTroqueladoRifa);
        rifasBonosContribucion.setTipoColorRifa(tipoColorRifa);

        return rifasBonosContribucionRepository.save(rifasBonosContribucion);
    }

    private void validarRifasBonosContribucionDTO(RifasBonosContribucionDTO rifasBonosContribucionDTO) {
        Assert.notNull(rifasBonosContribucionDTO.getMedida(), "La medida es un dato obligatorio.");
        Assert.notNull(rifasBonosContribucionDTO.getCantidad(), "La cantidad es un dato obligatorio.");
        Assert.notNull(rifasBonosContribucionDTO.getTipoPapelRifaId(), "El tipo de papel es un dato obligatorio.");
        Assert.notNull(rifasBonosContribucionDTO.getTipoTroqueladoRifaId(), "El tipo de troquelado es un dato obligatorio.");
        Assert.notNull(rifasBonosContribucionDTO.getTipoColorRifaId(), "El tipo de color es un dato obligatorio.");
    }

    private RifasBonosContribucion devolverRifasBonosContribucionCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    RifasBonosContribucion rifasBonosContribucionNueva = new RifasBonosContribucion();
                    rifasBonosContribucionNueva.setOrdenTrabajo(ordenTrabajo);
                    return rifasBonosContribucionNueva;
                });
    }

    public Optional<RifasBonosContribucion> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return rifasBonosContribucionRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        RifasBonosContribucion rifasBonosContribucion = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        rifasBonosContribucionRepository.deleteById(rifasBonosContribucion.getId());
    }
}
