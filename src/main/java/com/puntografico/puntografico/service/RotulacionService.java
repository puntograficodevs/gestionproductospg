package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.Rotulacion;
import com.puntografico.puntografico.domain.TipoCorteRotulacion;
import com.puntografico.puntografico.domain.TipoRotulacion;
import com.puntografico.puntografico.dto.RotulacionDTO;
import com.puntografico.puntografico.repository.RotulacionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class RotulacionService {

    private final RotulacionRepository rotulacionRepository;
    private final OpcionesRotulacionService opcionesRotulacionService;

    public Rotulacion guardar(RotulacionDTO rotulacionDTO, Long idRotulacion) {
        validarRotulacionDTO(rotulacionDTO);

        TipoRotulacion tipoRotulacion = opcionesRotulacionService.buscarTipoRotulacionPorId(rotulacionDTO.getTipoRotulacionId());
        TipoCorteRotulacion tipoCorteRotulacion = opcionesRotulacionService.buscarTipoCorteRotulacionPorId(rotulacionDTO.getTipoCorteRotulacionId());

        Rotulacion rotulacion = (idRotulacion != null) ? rotulacionRepository.findById(idRotulacion).get() : new Rotulacion();
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

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        rotulacionRepository.deleteById(id);
    }
}
