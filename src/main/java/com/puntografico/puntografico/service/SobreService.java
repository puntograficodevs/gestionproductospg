package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.CantidadSobre;
import com.puntografico.puntografico.domain.MedidaSobre;
import com.puntografico.puntografico.domain.Sobre;
import com.puntografico.puntografico.domain.TipoColorSobre;
import com.puntografico.puntografico.dto.SobreDTO;
import com.puntografico.puntografico.repository.SobreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class SobreService {

    private final SobreRepository sobreRepository;
    private final OpcionesSobreService opcionesSobreService;

    public Sobre guardar(SobreDTO sobreDTO, Long idSobre) {
        validarSobreDTO(sobreDTO);

        MedidaSobre medidaSobre = opcionesSobreService.buscarMedidaSobrePorId(sobreDTO.getMedidaSobreId());
        TipoColorSobre tipoColorSobre = opcionesSobreService.buscarTipoColorSobrePorId(sobreDTO.getTipoColorSobreId());
        CantidadSobre cantidadSobre = opcionesSobreService.buscarCantidadSobrePorId(sobreDTO.getCantidadSobreId());

        Integer cantidad = sobreDTO.getCantidad();

        if (cantidad == null || cantidad == 0 || cantidadSobre.getId() != 4) {
            cantidad = Integer.valueOf(cantidadSobre.getCantidad());
        }

        Sobre sobre = (idSobre != null) ? sobreRepository.findById(idSobre).get() : new Sobre();
        boolean adicionalDisenio = sobreDTO.getConAdicionalDisenio();

        sobre.setEnlaceArchivo(sobreDTO.getEnlaceArchivo());
        sobre.setConAdicionalDisenio(adicionalDisenio);
        sobre.setInformacionAdicional(sobreDTO.getInformacionAdicional());
        sobre.setMedidaSobre(medidaSobre);
        sobre.setTipoColorSobre(tipoColorSobre);
        sobre.setCantidadSobre(cantidadSobre);
        sobre.setCantidad(cantidad);

        if (medidaSobre.getMedida().equalsIgnoreCase("otra")) {
            sobre.setMedidaPersonalizada(sobreDTO.getMedidaPersonalizada());
        } else {
            sobre.setMedidaPersonalizada(null);
        }

        return sobreRepository.save(sobre);
    }

    private void validarSobreDTO(SobreDTO sobreDTO) {
        Assert.notNull(sobreDTO.getMedidaSobreId(), "medidaSobre es un dato obligatorio.");
        Assert.notNull(sobreDTO.getTipoColorSobreId(), "tipoColorSobre es un dato obligatorio.");
        Assert.notNull(sobreDTO.getCantidadSobreId(), "cantidadSobre es un dato obligatorio.");
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        sobreRepository.deleteById(id);
    }
}
