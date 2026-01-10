package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.Catalogo;
import com.puntografico.puntografico.domain.TipoFazCatalogo;
import com.puntografico.puntografico.domain.TipoLaminadoCatalogo;
import com.puntografico.puntografico.dto.CatalogoDTO;
import com.puntografico.puntografico.repository.CatalogoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class CatalogoService {

    private final CatalogoRepository catalogoRepository;

    private final OpcionesCatalogoService opcionesCatalogoService;

    public Catalogo guardar(CatalogoDTO catalogoDTO, Long idCatalogo) {
        validarCatalogoDTO(catalogoDTO);

        TipoFazCatalogo tipoFazCatalogo = opcionesCatalogoService.buscarTipoFazCatalogoPorId(catalogoDTO.getTipoFazCatalogoId());
        TipoLaminadoCatalogo tipoLaminadoCatalogo = opcionesCatalogoService.buscarTipoLaminadoCatalogoPorId(catalogoDTO.getTipoLaminadoCatalogoId());

        Catalogo catalogo = (idCatalogo != null) ? catalogoRepository.findById(idCatalogo).get() : new Catalogo();
        boolean adicionalDisenio = catalogoDTO.getConAdicionalDisenio();

        catalogo.setTipoPapel(catalogoDTO.getTipoPapel());
        catalogo.setTipoFazCatalogo(tipoFazCatalogo);
        catalogo.setTipoLaminadoCatalogo(tipoLaminadoCatalogo);
        catalogo.setEnlaceArchivo(catalogoDTO.getEnlaceArchivo());
        catalogo.setInformacionAdicional(catalogoDTO.getInformacionAdicional());
        catalogo.setConAdicionalDisenio(adicionalDisenio);
        catalogo.setCantidad(catalogoDTO.getCantidad());

        return catalogoRepository.save(catalogo);
    }

    private void validarCatalogoDTO(CatalogoDTO catalogoDTO) {
        Assert.notNull(catalogoDTO.getTipoPapel(), "El tipo de papel es un dato obligatorio.");
        Assert.notNull(catalogoDTO.getTipoLaminadoCatalogoId(), "El tipo de laminado es un dato obligatorio.");
        Assert.notNull(catalogoDTO.getTipoFazCatalogoId(), "El tipo de faz es un dato obligatorio.");
        Assert.notNull(catalogoDTO.getCantidad(), "La cantidad es un dato obligatorio.");
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        catalogoRepository.deleteById(id);
    }
}
