package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.RifasBonosContribucion;
import com.puntografico.puntografico.domain.TipoColorRifa;
import com.puntografico.puntografico.domain.TipoPapelRifa;
import com.puntografico.puntografico.domain.TipoTroqueladoRifa;
import com.puntografico.puntografico.dto.RifasBonosContribucionDTO;
import com.puntografico.puntografico.repository.RifasBonosContribucionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class RifasBonosContribucionService {

    private final RifasBonosContribucionRepository rifasBonosContribucionRepository;
    private final OpcionesRifasContribucionService opcionesRifasContribucionService;

    public RifasBonosContribucion guardar(RifasBonosContribucionDTO rifasBonosContribucionDTO, Long idRifasBonosContribucion) {
        validarRifasBonosContribucionDTO(rifasBonosContribucionDTO);

        TipoPapelRifa tipoPapelRifa = opcionesRifasContribucionService.buscarTipoPapelRifaPorId(rifasBonosContribucionDTO.getTipoPapelRifaId());
        TipoTroqueladoRifa tipoTroqueladoRifa = opcionesRifasContribucionService.buscarTipoTroqueladoRifaPorId(rifasBonosContribucionDTO.getTipoTroqueladoRifaId());
        TipoColorRifa tipoColorRifa = opcionesRifasContribucionService.buscarTipoColorRifaPorId(rifasBonosContribucionDTO.getTipoColorRifaId());

        RifasBonosContribucion rifasBonosContribucion = (idRifasBonosContribucion != null) ? rifasBonosContribucionRepository.findById(idRifasBonosContribucion).get() : new RifasBonosContribucion();
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

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        rifasBonosContribucionRepository.deleteById(id);
    }
}
