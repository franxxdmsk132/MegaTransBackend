package com.megatrans.megatransappbackend.Rutas.Repository;

import com.megatrans.megatransappbackend.Rutas.Entity.Rutas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RutasRepository extends JpaRepository<Rutas, Integer> {}
