package com.example.musteri_bakiye_seyri.service;

import com.example.musteri_bakiye_seyri.model.Musteri;
import com.example.musteri_bakiye_seyri.model.Fatura;
import com.example.musteri_bakiye_seyri.repository.MusteriRepository;
import com.example.musteri_bakiye_seyri.repository.FaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MusteriService {

    @Autowired
    private MusteriRepository musteriRepository;

    @Autowired
    private FaturaRepository faturaRepository;

    public Musteri getMusteriById(Long id) {
        return musteriRepository.findById(id).orElse(null);
    }

    public List<Musteri> getAllMusteriler() {
        return musteriRepository.findAll();
    }

    public Date getMaxBorcluTarihi(Long musteriId) {
    	List<Fatura> faturalar = faturaRepository.findByMusteriId(musteriId);

        List<Fatura> unpaidFaturalar = faturalar.stream()
                .filter(fatura -> fatura.getOdemeTarihi() == null)
                .sorted(Comparator.comparing(Fatura::getFaturaTarihi))
                .collect(Collectors.toList());

        double toplamBorç = 0.0;
        double maxBorç = 0.0;
        Date maxBorçTarihi = null;

        for (Fatura fatura : unpaidFaturalar) {
            toplamBorç += fatura.getFaturaTutari();

            if (toplamBorç > maxBorç) {
                maxBorç = toplamBorç;
                maxBorçTarihi = fatura.getFaturaTarihi();
            }
        }

        return maxBorç > 0 ? maxBorçTarihi : null;
    }
}
