package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.FlybannerDTO;
import com.puntografico.puntografico.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional @AllArgsConstructor
public class FlybannerService {

    private final FlybannerRepository flybannerRepository;
    private final OpcionesFlybannerService opcionesFlybannerService;
    private final OrdenTrabajoRepository ordenTrabajoRepository;

    public Flybanner guardar(FlybannerDTO flybannerDTO, Long idOrdenTrabajo) {
        validarFlybannerDTO(flybannerDTO);
        Flybanner flybanner = devolverFlybannerCorrespondiente(idOrdenTrabajo);

        TipoFazFlybanner tipoFazFlybanner = opcionesFlybannerService.buscarTipoFazFlybannerPorId(flybannerDTO.getTipoFazFlybannerId());
        AlturaFlybanner alturaFlybanner = opcionesFlybannerService.buscarAlturaFlybannerPorId(flybannerDTO.getAlturaFlybannerId());
        BanderaFlybanner banderaFlybanner = opcionesFlybannerService.buscarBanderaFlybannerPorId(flybannerDTO.getBanderaFlybannerId());
        TipoBaseFlybanner tipoBaseFlybanner = opcionesFlybannerService.buscarTipoBaseFlybannerPorId(flybannerDTO.getTipoBaseFlybannerId());

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

    private Flybanner devolverFlybannerCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    Flybanner flybannerNuevo = new Flybanner();
                    flybannerNuevo.setOrdenTrabajo(ordenTrabajo);
                    return flybannerNuevo;
                });
    }

    public Optional<Flybanner> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return flybannerRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        Flybanner flybanner = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        flybannerRepository.deleteById(flybanner.getId());
    }
}
