package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.ComboDTO;
import com.puntografico.puntografico.repository.ComboRepository;
import com.puntografico.puntografico.repository.OrdenTrabajoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional @AllArgsConstructor
public class ComboService {

    private final ComboRepository comboRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final OpcionesComboService opcionesComboService;

    public Combo guardar(ComboDTO comboDTO, Long idOrdenTrabajo) {
        validarComboDTO(comboDTO);
        Combo combo = devolverComboCorrespondiente(idOrdenTrabajo);

        TipoCombo tipoCombo = opcionesComboService.buscarTipoComboPorId(comboDTO.getTipoComboId());

        boolean adicionalDisenio = comboDTO.getConAdicionalDisenio();

        combo.setEnlaceArchivo(comboDTO.getEnlaceArchivo());
        combo.setConAdicionalDisenio(adicionalDisenio);
        combo.setInformacionAdicional(comboDTO.getInformacionAdicional());
        combo.setTipoCombo(tipoCombo);
        combo.setCantidad(comboDTO.getCantidad());

        return comboRepository.save(combo);
    }

    private void validarComboDTO(ComboDTO comboDTO) {
        Assert.notNull(comboDTO.getTipoComboId(), "El tipo de combo es un dato obligatorio.");
        Assert.notNull(comboDTO.getCantidad(), "La cantidad es un dato obligatorio.");
    }

    private Combo devolverComboCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    Combo comboNuevo = new Combo();
                    comboNuevo.setOrdenTrabajo(ordenTrabajo);
                    return comboNuevo;
                });
    }

    public Optional<Combo> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return comboRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        Combo combo = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        comboRepository.deleteById(combo.getId());
    }
}
