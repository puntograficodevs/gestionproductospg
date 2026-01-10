package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.LonaComunDTO;
import com.puntografico.puntografico.repository.LonaComunRepository;
import com.puntografico.puntografico.repository.OrdenTrabajoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

@Service @Transactional @AllArgsConstructor
public class LonaComunService {

    private final LonaComunRepository lonaComunRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final OpcionesLonaComunService opcionesLonaComunService;

    public LonaComun guardar(LonaComunDTO lonaComunDTO, Long idOrdenTrabajo) {
        validarLonaComunDTO(lonaComunDTO);
        LonaComun lonaComun = devolverLonaComunCorrespondiente(idOrdenTrabajo);

        MedidaLonaComun medidaLonaComun = opcionesLonaComunService.buscarMedidaLonaComunPorId(lonaComunDTO.getMedidaLonaComunId());
        TipoLonaComun tipoLonaComun = opcionesLonaComunService.buscarTipoLonaComunPorId(lonaComunDTO.getTipoLonaComunId());

        boolean conOjales = lonaComunDTO.getConOjales();
        boolean conOjalesConRefuerzo = lonaComunDTO.getConOjalesConRefuerzo();
        boolean conBolsillos =  lonaComunDTO.getConBolsillos();
        boolean conDemasiaParaTensado = lonaComunDTO.getConDemasiaParaTensado();
        boolean conSolapado = lonaComunDTO.getConSolapado();
        boolean adicionalDisenio = lonaComunDTO.getConAdicionalDisenio();

        lonaComun.setMedidaLonaComun(medidaLonaComun);
        lonaComun.setTipoLonaComun(tipoLonaComun);
        lonaComun.setConOjales(conOjales);
        lonaComun.setConOjalesConRefuerzo(conOjalesConRefuerzo);
        lonaComun.setConBolsillos(conBolsillos);
        lonaComun.setConDemasiaParaTensado(conDemasiaParaTensado);
        lonaComun.setConSolapado(conSolapado);
        lonaComun.setConAdicionalDisenio(adicionalDisenio);
        lonaComun.setEnlaceArchivo(lonaComunDTO.getEnlaceArchivo());
        lonaComun.setInformacionAdicional(lonaComunDTO.getInformacionAdicional());
        lonaComun.setCantidad(lonaComunDTO.getCantidad());

        if (medidaLonaComun.getMedida().equalsIgnoreCase("otra")) {
            lonaComun.setMedidaPersonalizada(lonaComunDTO.getMedidaPersonalizada());
        } else {
            lonaComun.setMedidaPersonalizada(null);
        }

        return lonaComunRepository.save(lonaComun);
    }

    private void validarLonaComunDTO(LonaComunDTO lonaComunDTO) {
        Assert.notNull(lonaComunDTO.getMedidaLonaComunId(), "La medida es un dato obligatorio.");
        Assert.notNull(lonaComunDTO.getTipoLonaComunId(), "El tipo de lona es un dato obligatorio.");
        Assert.notNull(lonaComunDTO.getCantidad(), "La cantidad es un dato obligatorio.");
    }

    private LonaComun devolverLonaComunCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    LonaComun lonaComunNueva = new LonaComun();
                    lonaComunNueva.setOrdenTrabajo(ordenTrabajo);
                    return lonaComunNueva;
                });
    }

    public Optional<LonaComun> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return lonaComunRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        LonaComun lonaComun = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        lonaComunRepository.deleteById(lonaComun.getId());
    }
}
