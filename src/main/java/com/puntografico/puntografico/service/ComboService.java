package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.Combo;
import com.puntografico.puntografico.domain.TipoCombo;
import com.puntografico.puntografico.dto.ComboDTO;
import com.puntografico.puntografico.repository.ComboRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.transaction.Transactional;

@Service
@Transactional @AllArgsConstructor
public class ComboService {

    private final ComboRepository comboRepository;

    private final OpcionesComboService opcionesComboService;

    public Combo guardar(ComboDTO comboDTO, Long idCombo) {
        validarComboDTO(comboDTO);

        TipoCombo tipoCombo = opcionesComboService.buscarTipoComboPorId(comboDTO.getTipoComboId());

        Combo combo = (idCombo != null) ? comboRepository.findById(idCombo).get() : new Combo();
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

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        comboRepository.deleteById(id);
    }
}
