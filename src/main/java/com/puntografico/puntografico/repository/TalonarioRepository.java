package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.Talonario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TalonarioRepository extends JpaRepository<Talonario, Long> {
}
