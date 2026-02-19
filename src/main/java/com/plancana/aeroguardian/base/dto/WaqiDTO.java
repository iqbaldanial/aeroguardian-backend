package com.plancana.aeroguardian.base.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WaqiDTO(String status, WaqiData data) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record WaqiData(
            int aqi,
            City city
    ){}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record City(
            @JsonProperty("geo")
            List<Double> coordinates
    ){}
}
