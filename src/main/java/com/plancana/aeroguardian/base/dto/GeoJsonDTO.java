package com.plancana.aeroguardian.base.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.locationtech.jts.geom.Geometry;
import org.n52.jackson.datatype.jts.GeometryDeserializer;
import org.n52.jackson.datatype.jts.GeometrySerializer;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public record GeoJsonDTO() {


    @JsonIgnoreProperties(ignoreUnknown = true)
    public record GeoJsonCollection(List<GeoJsonFeature> features){}


    @JsonIgnoreProperties(ignoreUnknown = true)
    public record GeoJsonFeature(
            Map<String, Object> properties,
            @JsonDeserialize(using = GeometryDeserializer.class)
            Geometry geometry
    ){}
}
