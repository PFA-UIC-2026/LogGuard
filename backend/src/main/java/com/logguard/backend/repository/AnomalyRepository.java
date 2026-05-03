package com.logguard.backend.repository;

import com.logguard.backend.model.Anomaly;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnomalyRepository extends JpaRepository<Anomaly, Long> {
    List<Anomaly> findAllByOrderByDetectedAtDesc();
}
