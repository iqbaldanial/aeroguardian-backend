package com.plancana.aeroguardian.base.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sensors")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AirSensor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "sensor_code", unique = true, nullable = false)
    private String sensorCode;

    private Double aqiLevel;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point location;

    private LocalDateTime lastReading;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastReading = LocalDateTime.now();
    }
}
