package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.LonaPublicitariaDTO;
import com.puntografico.puntografico.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

@Service @Transactional @AllArgsConstructor
public class LonaPublicitariaService {

    private final LonaPublicitariaRepository lonaPublicitariaRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final OpcionesLonaPublicitariaService opcionesLonaPublicitariaService;

    public LonaPublicitaria guardar(LonaPublicitariaDTO lonaPublicitariaDTO, Long idOrdenTrabajo) {
        validarLonaPublicitariaDTO(lonaPublicitariaDTO);
        LonaPublicitaria lonaPublicitaria = devolverLonaPublicitariaCorrespondiente(idOrdenTrabajo);

        MedidaLonaPublicitaria medidaLonaPublicitaria = opcionesLonaPublicitariaService.buscarMedidaLonaPublicitariaPorId(lonaPublicitariaDTO.getMedidaLonaPublicitariaId());
        TipoLonaPublicitaria tipoLonaPublicitaria = opcionesLonaPublicitariaService.buscarTipoLonaPublicitariaPorId(lonaPublicitariaDTO.getTipoLonaPublicitariaId());

        boolean conAdicionalPortabanner = lonaPublicitariaDTO.getConAdicionalPortabanner();
        boolean conOjales = lonaPublicitariaDTO.getConOjales();
        boolean conOjalesConRefuerzo = lonaPublicitariaDTO.getConOjalesConRefuerzo();
        boolean conBolsillos = lonaPublicitariaDTO.getConBolsillos();
        boolean conDemasiaParaTensado = lonaPublicitariaDTO.getConDemasiaParaTensado();
        boolean conSolapado = lonaPublicitariaDTO.getConSolapado();
        boolean adicionalDisenio = lonaPublicitariaDTO.getConAdicionalDisenio();


        lonaPublicitaria.setMedidaLonaPublicitaria(medidaLonaPublicitaria);
        lonaPublicitaria.setTipoLonaPublicitaria(tipoLonaPublicitaria);
        lonaPublicitaria.setConAdicionalPortabanner(conAdicionalPortabanner);
        lonaPublicitaria.setConOjales(conOjales);
        lonaPublicitaria.setConOjalesConRefuerzo(conOjalesConRefuerzo);
        lonaPublicitaria.setConBolsillos(conBolsillos);
        lonaPublicitaria.setConDemasiaParaTensado(conDemasiaParaTensado);
        lonaPublicitaria.setConSolapado(conSolapado);
        lonaPublicitaria.setConAdicionalDisenio(adicionalDisenio);
        lonaPublicitaria.setEnlaceArchivo(lonaPublicitariaDTO.getEnlaceArchivo());
        lonaPublicitaria.setInformacionAdicional(lonaPublicitariaDTO.getInformacionAdicional());
        lonaPublicitaria.setCantidad(lonaPublicitariaDTO.getCantidad());

        return lonaPublicitariaRepository.save(lonaPublicitaria);
    }

    private void validarLonaPublicitariaDTO(LonaPublicitariaDTO lonaPublicitariaDTO) {
        Assert.notNull(lonaPublicitariaDTO.getMedidaLonaPublicitariaId(), "La medida es un dato obligatorio.");
        Assert.notNull(lonaPublicitariaDTO.getTipoLonaPublicitariaId(), "El tipo de lona es un dato obligatorio.");
        Assert.notNull(lonaPublicitariaDTO.getCantidad(), "La cantidad es un dato obligatorio.");
    }

    private LonaPublicitaria devolverLonaPublicitariaCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    LonaPublicitaria lonaPublicitariaNueva = new LonaPublicitaria();
                    lonaPublicitariaNueva.setOrdenTrabajo(ordenTrabajo);
                    return lonaPublicitariaNueva;
                });
    }

    public Optional<LonaPublicitaria> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return lonaPublicitariaRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        LonaPublicitaria lonaPublicitaria = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        lonaPublicitariaRepository.deleteById(lonaPublicitaria.getId());
    }
}
