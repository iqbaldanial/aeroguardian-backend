package com.plancana.aeroguardian.base.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record AirSensorRequest(
        @NotBlank(message = "Sensor code is required")
        String sensorCode,

        @NotNull(message = "AQI level is required")
        Double aqiLevel,

        @NotNull(message = "Location is required")
        Double latitude,

        @NotNull(message = "Location is required")
        Double longitude
) {}
