package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.TipoFazCarpetaSolapa;
import com.puntografico.puntografico.domain.TipoLaminadoCarpetaSolapa;
import com.puntografico.puntografico.repository.TipoFazCarpetaSolapaRepository;
import com.puntografico.puntografico.repository.TipoLaminadoCarpetaSolapaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional @AllArgsConstructor
public class OpcionesCarpetaSolapaService {

    private final TipoLaminadoCarpetaSolapaRepository tipoLaminadoCarpetaSolapaRepository;

    private final TipoFazCarpetaSolapaRepository tipoFazCarpetaSolapaRepository;

    public List<TipoLaminadoCarpetaSolapa> buscarTodosTipoLaminadoCarpetaSolapa() {
        return tipoLaminadoCarpetaSolapaRepository.findAll();
    }

    public List<TipoFazCarpetaSolapa> buscarTodosTipoFazCarpetaSolapa() {
        return tipoFazCarpetaSolapaRepository.findAll();
    }

    public TipoLaminadoCarpetaSolapa buscarTipoLaminadoCarpetaSolapaPorId(Long id) {
        return tipoLaminadoCarpetaSolapaRepository.findById(id).get();
    }

    public TipoFazCarpetaSolapa buscarTipoFazCarpetaSolapaPorId(Long id) {
        return tipoFazCarpetaSolapaRepository.findById(id).get();
    }
}
