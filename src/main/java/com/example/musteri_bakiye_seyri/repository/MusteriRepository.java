package com.example.musteri_bakiye_seyri.repository;

import com.example.musteri_bakiye_seyri.model.Musteri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusteriRepository extends JpaRepository<Musteri, Long> {
}
