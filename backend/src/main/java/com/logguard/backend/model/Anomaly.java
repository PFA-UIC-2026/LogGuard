package com.logguard.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "anomalies")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Anomaly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime detectedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnomalyType type;

    @Column(nullable = false, length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trigger_log_id")
    private LogEntry triggerLog;
}
