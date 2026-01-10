package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Optional<Empleado> findByUsernameAndPassword(String username, String password);
    Optional<Empleado> findByUsername(String username);
}
