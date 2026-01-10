package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.CarpetaSolapaDTO;
import com.puntografico.puntografico.repository.CarpetaSolapaRepository;
import com.puntografico.puntografico.repository.OrdenTrabajoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class CarpetaSolapaService {

    private final CarpetaSolapaRepository carpetaSolapaRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final OpcionesCarpetaSolapaService opcionesCarpetaSolapaService;

    public CarpetaSolapa guardar(CarpetaSolapaDTO carpetaSolapaDTO, Long idOrdenTrabajo) {
        validarCarpetaSolapaDTO(carpetaSolapaDTO);
        CarpetaSolapa carpetaSolapa = devolverCarpetaSolapaCorrespondiente(idOrdenTrabajo);

        TipoLaminadoCarpetaSolapa tipoLaminadoCarpetaSolapa = opcionesCarpetaSolapaService.buscarTipoLaminadoCarpetaSolapaPorId(carpetaSolapaDTO.getTipoLaminadoCarpetaSolapaId());
        TipoFazCarpetaSolapa tipoFazCarpetaSolapa = opcionesCarpetaSolapaService.buscarTipoFazCarpetaSolapaPorId(carpetaSolapaDTO.getTipoFazCarpetaSolapaId());

        boolean adicionalDisenio = carpetaSolapaDTO.getConAdicionalDisenio();

        carpetaSolapa.setTipoPapel(carpetaSolapaDTO.getTipoPapel());
        carpetaSolapa.setCantidad(carpetaSolapaDTO.getCantidad());
        carpetaSolapa.setEnlaceArchivo(carpetaSolapaDTO.getEnlaceArchivo());
        carpetaSolapa.setConAdicionalDisenio(adicionalDisenio);
        carpetaSolapa.setInformacionAdicional(carpetaSolapaDTO.getInformacionAdicional());
        carpetaSolapa.setTipoLaminadoCarpetaSolapa(tipoLaminadoCarpetaSolapa);
        carpetaSolapa.setTipoFazCarpetaSolapa(tipoFazCarpetaSolapa);

        return carpetaSolapaRepository.save(carpetaSolapa);
    }

    private void validarCarpetaSolapaDTO(CarpetaSolapaDTO carpetaSolapaDTO) {
        Assert.notNull(carpetaSolapaDTO.getTipoPapel(), "El tipo de papel es un dato obligatorio.");
        Assert.notNull(carpetaSolapaDTO.getCantidad(), "La cantidad es un dato obligatorio.");
        Assert.notNull(carpetaSolapaDTO.getTipoLaminadoCarpetaSolapaId(), "El tipo de laminado es un dato obligatorio.");
        Assert.notNull(carpetaSolapaDTO.getTipoFazCarpetaSolapaId(), "El tipo de faz es un dato obligatorio.");
    }

    private CarpetaSolapa devolverCarpetaSolapaCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    CarpetaSolapa carpetaSolapaNueva = new CarpetaSolapa();
                    carpetaSolapaNueva.setOrdenTrabajo(ordenTrabajo);
                    return carpetaSolapaNueva;
                });
    }

    public Optional<CarpetaSolapa> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return carpetaSolapaRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        CarpetaSolapa carpetaSolapa = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        carpetaSolapaRepository.deleteById(carpetaSolapa.getId());
    }
}
