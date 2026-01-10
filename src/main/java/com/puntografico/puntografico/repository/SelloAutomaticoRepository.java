package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.SelloAutomatico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelloAutomaticoRepository extends JpaRepository<SelloAutomatico, Long> {
}
