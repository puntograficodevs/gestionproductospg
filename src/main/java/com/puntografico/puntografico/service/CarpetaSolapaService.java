package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.CarpetaSolapa;
import com.puntografico.puntografico.domain.TipoFazCarpetaSolapa;
import com.puntografico.puntografico.domain.TipoLaminadoCarpetaSolapa;
import com.puntografico.puntografico.dto.CarpetaSolapaDTO;
import com.puntografico.puntografico.repository.CarpetaSolapaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class CarpetaSolapaService {

    private final CarpetaSolapaRepository carpetaSolapaRepository;

    private final OpcionesCarpetaSolapaService opcionesCarpetaSolapaService;

    public CarpetaSolapa guardar(CarpetaSolapaDTO carpetaSolapaDTO, Long idCarpetaSolapa) {
        validarCarpetaSolapaDTO(carpetaSolapaDTO);

        TipoLaminadoCarpetaSolapa tipoLaminadoCarpetaSolapa = opcionesCarpetaSolapaService.buscarTipoLaminadoCarpetaSolapaPorId(carpetaSolapaDTO.getTipoLaminadoCarpetaSolapaId());
        TipoFazCarpetaSolapa tipoFazCarpetaSolapa = opcionesCarpetaSolapaService.buscarTipoFazCarpetaSolapaPorId(carpetaSolapaDTO.getTipoFazCarpetaSolapaId());

        CarpetaSolapa carpetaSolapa = (idCarpetaSolapa != null) ? carpetaSolapaRepository.findById(idCarpetaSolapa).get() : new CarpetaSolapa();
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

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        carpetaSolapaRepository.deleteById(id);
    }
}
