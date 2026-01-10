package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.ImpresionDTO;
import com.puntografico.puntografico.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class ImpresionService {

    private final ImpresionRepository impresionRepository;

    private final OpcionesImpresionService opcionesImpresionService;

    public Impresion guardar(ImpresionDTO impresionDTO, Long idImpresion) {
        validarImpresionDTO(impresionDTO);

        TipoColorImpresion tipoColorImpresion = opcionesImpresionService.buscarTipoColorImpresionPorId(impresionDTO.getTipoColorImpresionId());
        TamanioHojaImpresion tamanioHojaImpresion = opcionesImpresionService.buscarTamanioHojaImpresionPorId(impresionDTO.getTamanioHojaImpresionId());
        TipoFazImpresion tipoFazImpresion = opcionesImpresionService.buscarTipoFazImpresionPorId(impresionDTO.getTipoFazImpresionId());
        TipoPapelImpresion tipoPapelImpresion = opcionesImpresionService.buscarTipoPapelImpresionPorId(impresionDTO.getTipoPapelImpresionId());
        TipoImpresion tipoImpresion = opcionesImpresionService.buscarTipoImpresionPorId(impresionDTO.getTipoImpresionId());
        CantidadImpresion cantidadImpresion = opcionesImpresionService.buscarCantidadImpresionPorId(impresionDTO.getCantidadImpresionId());

        Impresion impresion = (idImpresion != null) ? impresionRepository.findById(idImpresion).get() : new Impresion();
        boolean esAnillado = impresionDTO.getEsAnillado();

        impresion.setEsAnillado(esAnillado);
        impresion.setEnlaceArchivo(impresionDTO.getEnlaceArchivo());
        impresion.setInformacionAdicional(impresionDTO.getInformacionAdicional());
        impresion.setTipoColorImpresion(tipoColorImpresion);
        impresion.setTamanioHojaImpresion(tamanioHojaImpresion);
        impresion.setTipoFazImpresion(tipoFazImpresion);
        impresion.setTipoPapelImpresion(tipoPapelImpresion);
        impresion.setCantidadImpresion(cantidadImpresion);
        impresion.setTipoImpresion(tipoImpresion);
        impresion.setCantidad(impresionDTO.getCantidad());

        return impresionRepository.save(impresion);
    }

    private void validarImpresionDTO(ImpresionDTO impresionDTO) {
        Assert.notNull(impresionDTO.getTipoColorImpresionId(), "tipoColorImpresionString es un campo obligatorio.");
        Assert.notNull(impresionDTO.getTamanioHojaImpresionId(), "tamanioHojaImpresionString es un campo obligatorio.");
        Assert.notNull(impresionDTO.getTipoFazImpresionId(), "tipoFazImpresionString es un campo obligatorio.");
        Assert.notNull(impresionDTO.getTipoPapelImpresionId(), "tipoPapelImpresionString es un campo obligatorio.");
        Assert.notNull(impresionDTO.getTipoImpresionId(), "tipoImpresionString es un campo obligatorio.");
        Assert.notNull(impresionDTO.getCantidad(), "cantidadString es un campo obligatorio.");
        Assert.notNull(impresionDTO.getCantidadImpresionId(), "cantidadImpresionString es un campo obligatorio.");
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        impresionRepository.deleteById(id);
    }
}

