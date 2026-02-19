package com.plancana.aeroguardian.base.entity;


import com.plancana.aeroguardian.base.enums.AqiLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "alerts")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "zone_id", nullable = false)
    private VulnerableZone zone;

    private String stationName;
    private Double aqiValue;
    private String zoneName;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private AqiLevel aqiLevel;

}
