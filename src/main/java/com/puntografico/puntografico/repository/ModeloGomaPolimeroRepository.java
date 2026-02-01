package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.ModeloGomaPolimero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeloGomaPolimeroRepository extends JpaRepository<ModeloGomaPolimero, Long>  {
}
