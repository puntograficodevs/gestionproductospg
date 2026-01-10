package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.FlybannerDTO;
import com.puntografico.puntografico.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service
@Transactional @AllArgsConstructor
public class FlybannerService {

    private final FlybannerRepository flybannerRepository;
    private final OpcionesFlybannerService opcionesFlybannerService;

    public Flybanner guardar(FlybannerDTO flybannerDTO, Long idFlybanner) {
        validarFlybannerDTO(flybannerDTO);

        TipoFazFlybanner tipoFazFlybanner = opcionesFlybannerService.buscarTipoFazFlybannerPorId(flybannerDTO.getTipoFazFlybannerId());
        AlturaFlybanner alturaFlybanner = opcionesFlybannerService.buscarAlturaFlybannerPorId(flybannerDTO.getAlturaFlybannerId());
        BanderaFlybanner banderaFlybanner = opcionesFlybannerService.buscarBanderaFlybannerPorId(flybannerDTO.getBanderaFlybannerId());
        TipoBaseFlybanner tipoBaseFlybanner = opcionesFlybannerService.buscarTipoBaseFlybannerPorId(flybannerDTO.getTipoBaseFlybannerId());

        Flybanner flybanner = (idFlybanner != null) ? flybannerRepository.findById(idFlybanner).get() : new Flybanner();
        boolean adicionalDisenio = flybannerDTO.getConAdicionalDisenio();

        flybanner.setTipoFazFlybanner(tipoFazFlybanner);
        flybanner.setAlturaFlybanner(alturaFlybanner);
        flybanner.setBanderaFlybanner(banderaFlybanner);
        flybanner.setTipoBaseFlybanner(tipoBaseFlybanner);
        flybanner.setCantidad(flybannerDTO.getCantidad());
        flybanner.setEnlaceArchivo(flybannerDTO.getEnlaceArchivo());
        flybanner.setInformacionAdicional(flybannerDTO.getInformacionAdicional());
        flybanner.setConAdicionalDisenio(adicionalDisenio);

        return flybannerRepository.save(flybanner);
    }

    private void validarFlybannerDTO(FlybannerDTO flybannerDTO) {
        Assert.notNull(flybannerDTO.getTipoFazFlybannerId(), "tipoFazFlybannerString es un dato obligatorio.");
        Assert.notNull(flybannerDTO.getAlturaFlybannerId(), "alturaFlybannerString es un dato obligatorio.");
        Assert.notNull(flybannerDTO.getBanderaFlybannerId(), "banderaFlybannerString es un dato obligatorio.");
        Assert.notNull(flybannerDTO.getTipoBaseFlybannerId(), "tipoBaseFlybannerString es un dato obligatorio.");
        Assert.notNull(flybannerDTO.getCantidad(), "cantidadString es un dato obligatorio.");
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        flybannerRepository.deleteById(id);
    }
}
