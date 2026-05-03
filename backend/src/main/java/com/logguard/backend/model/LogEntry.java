package com.logguard.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "log_entries")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LogLevel level;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false, length = 2000)
    private String message;

    @Column(columnDefinition = "TEXT")
    private String rawPayload;
}
