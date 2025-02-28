package com.megatrans.megatransappbackend.Lote.Repository;

import com.megatrans.megatransappbackend.Lote.Entity.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Integer> {

    @Query("SELECT l.numLote FROM Lote l ORDER BY l.numLote DESC LIMIT 1")
    Optional<String> findLastNumLote();
}