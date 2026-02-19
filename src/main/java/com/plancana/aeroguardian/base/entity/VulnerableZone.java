package com.plancana.aeroguardian.base.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.n52.jackson.datatype.jts.GeometrySerializer;

import java.util.UUID;

@Entity
@Table(name = "vulnerable_zones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VulnerableZone {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name; // e.g., "Kuala Lumpur Primary School"

    private String zoneType; // e.g., "SCHOOL", "HOSPITAL"

    @Column(columnDefinition = "geometry(Geometry, 4326)")
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonIgnoreProperties({"envelope", "boundary", "envelopeInternal", "factory", "precisionModel", "numPoints", "coordinate", "coordinates"})
    private Geometry area; // The actual boundary of the school
}

