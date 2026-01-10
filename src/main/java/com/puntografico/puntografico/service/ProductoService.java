package com.puntografico.puntografico.service;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service @Transactional
public class ProductoService {

    public Long buscarOrdenIdSiExiste(String idOrdenDesdeRequest) {
        Long idOrden = null;

        if (idOrdenDesdeRequest != null && !idOrdenDesdeRequest.isBlank()) {
            try {
                idOrden = Long.parseLong(idOrdenDesdeRequest);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Id Orden inválido: " + idOrdenDesdeRequest);
            }
        }

        return idOrden;
    }
}
