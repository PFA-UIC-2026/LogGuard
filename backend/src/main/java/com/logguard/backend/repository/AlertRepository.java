package com.logguard.backend.repository;

import com.logguard.backend.model.Alert;
import com.logguard.backend.model.AlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findAllByOrderByCreatedAtDesc();
    List<Alert> findByStatusOrderByCreatedAtDesc(AlertStatus status);
}
