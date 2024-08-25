package com.example.musteri_bakiye_seyri.repository;

import com.example.musteri_bakiye_seyri.model.Fatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaturaRepository extends JpaRepository<Fatura, Long> {
    List<Fatura> findByMusteriId(Long musteriId);
}
