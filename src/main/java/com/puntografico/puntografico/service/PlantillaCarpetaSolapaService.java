package com.puntografico.puntografico.service;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class PlantillaCarpetaSolapaService {

    public Long buscarIdCantidadCarpetaSolapa(int cantidad) {
        long idCantidadCarpetaSolapa;

        if(cantidad < 50) {
            idCantidadCarpetaSolapa = 1L;
        } else if (cantidad == 50) {
            idCantidadCarpetaSolapa = 2L;
        } else if (cantidad == 100) {
            idCantidadCarpetaSolapa = 3L;
        } else if (cantidad == 150) {
            idCantidadCarpetaSolapa = 4L;
        } else {
            idCantidadCarpetaSolapa = 5L;
        }

        return idCantidadCarpetaSolapa;
    }
}
