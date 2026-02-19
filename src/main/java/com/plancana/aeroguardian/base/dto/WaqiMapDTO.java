package com.plancana.aeroguardian.base.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.plancana.aeroguardian.base.enums.AqiLevel;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record WaqiMapDTO(
        String status,
        List<Station> data
) {
    public record Station(
            double lat,
            double lon,
            int uid,
            String aqi,
            StationData station
    ){
        @JsonProperty("aqiLevel")
        public AqiLevel aqiLevel(){
            if("-".equals(aqi)) return null;
            return AqiLevel.fromAQI(Double.parseDouble(aqi));
        }
    }

    public record StationData(
            String name,
            String time
    ){}
}




