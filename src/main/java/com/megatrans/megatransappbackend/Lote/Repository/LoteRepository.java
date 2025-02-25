package com.megatrans.megatransappbackend.Lote.Repository;

import com.megatrans.megatransappbackend.Lote.Entity.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface LoteRepository extends JpaRepository <Lote, Integer> { }
