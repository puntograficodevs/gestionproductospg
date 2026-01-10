package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.TraeMaterialVinilo;
import com.puntografico.puntografico.domain.ViniloDeCorte;
import com.puntografico.puntografico.dto.ViniloDeCorteDTO;
import com.puntografico.puntografico.repository.ViniloDeCorteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class ViniloDeCorteService {

    private final ViniloDeCorteRepository viniloDeCorteRepository;

    private final OpcionesViniloDeCorteService opcionesViniloDeCorteService;

    public ViniloDeCorte guardar(ViniloDeCorteDTO viniloDeCorteDTO, Long idViniloDeCorte) {
        validarViniloDeCorteDTO(viniloDeCorteDTO);

        TraeMaterialVinilo traeMaterialVinilo = opcionesViniloDeCorteService.buscarTraeMaterialViniloPorId(viniloDeCorteDTO.getTraeMaterialViniloId());

        ViniloDeCorte viniloDeCorte = (idViniloDeCorte != null) ? viniloDeCorteRepository.findById(idViniloDeCorte).get() : new ViniloDeCorte();
        boolean adicionalDisenio = viniloDeCorteDTO.getConAdicionalDisenio();
        boolean esPromocional = viniloDeCorteDTO.getEsPromocional();
        boolean esOracal = viniloDeCorteDTO.getEsOracal();
        boolean conColocacion = viniloDeCorteDTO.getConColocacion();

        viniloDeCorte.setEsPromocional(esPromocional);
        viniloDeCorte.setEsOracal(esOracal);
        viniloDeCorte.setCodigoColor(viniloDeCorteDTO.getCodigoColor());
        viniloDeCorte.setConColocacion(conColocacion);
        viniloDeCorte.setMedida(viniloDeCorteDTO.getMedida());
        viniloDeCorte.setEnlaceArchivo(viniloDeCorteDTO.getEnlaceArchivo());
        viniloDeCorte.setConAdicionalDisenio(adicionalDisenio);
        viniloDeCorte.setInformacionAdicional(viniloDeCorteDTO.getInformacionAdicional());
        viniloDeCorte.setTraeMaterialVinilo(traeMaterialVinilo);
        viniloDeCorte.setCantidad(viniloDeCorteDTO.getCantidad());

        return viniloDeCorteRepository.save(viniloDeCorte);
    }

    private void validarViniloDeCorteDTO(ViniloDeCorteDTO viniloDeCorteDTO) {
        Assert.notNull(viniloDeCorteDTO.getTraeMaterialViniloId(), "traeMaterialViniloString es un dato obligatorio.");
        Assert.notNull(viniloDeCorteDTO.getCantidad(), "cantidadString es un dato obligatorio.");
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        viniloDeCorteRepository.deleteById(id);
    }
}
