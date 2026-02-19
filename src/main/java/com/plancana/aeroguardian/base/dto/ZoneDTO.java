package com.plancana.aeroguardian.base.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.locationtech.jts.geom.Geometry;
import org.n52.jackson.datatype.jts.GeometryDeserializer;
import org.n52.jackson.datatype.jts.GeometrySerializer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ZoneDTO(
        UUID id,
        String name,
        String zoneType,
        Object geometry
){}
