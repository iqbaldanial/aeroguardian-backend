package com.plancana.aeroguardian.base.dto;


import com.plancana.aeroguardian.base.enums.AqiLevel;


import java.time.LocalDateTime;


public record AlertDTO(
//        VulnerableZone zone,
        String stationName,
        Double aqiValue,
        String zoneName,
        LocalDateTime timestamp,
        AqiLevel aqiLevel
) {

}
