package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.LonaComun;
import com.puntografico.puntografico.domain.MedidaLonaComun;
import com.puntografico.puntografico.domain.TipoLonaComun;
import com.puntografico.puntografico.dto.LonaComunDTO;
import com.puntografico.puntografico.repository.LonaComunRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class LonaComunService {

    private final LonaComunRepository lonaComunRepository;

    private final OpcionesLonaComunService opcionesLonaComunService;

    public LonaComun guardar(LonaComunDTO lonaComunDTO, Long idLonaComun) {
        validarLonaComunDTO(lonaComunDTO);

        MedidaLonaComun medidaLonaComun = opcionesLonaComunService.buscarMedidaLonaComunPorId(lonaComunDTO.getMedidaLonaComunId());
        TipoLonaComun tipoLonaComun = opcionesLonaComunService.buscarTipoLonaComunPorId(lonaComunDTO.getTipoLonaComunId());

        LonaComun lonaComun = (idLonaComun != null) ? lonaComunRepository.findById(idLonaComun).get() :  new LonaComun();
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

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        lonaComunRepository.deleteById(id);
    }
}
