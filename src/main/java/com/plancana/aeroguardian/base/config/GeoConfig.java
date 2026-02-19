package com.plancana.aeroguardian.base.config;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeoConfig {

    @Bean
    public GeometryFactory geometryFactory() {
        // SRID 4326 is the standard for GPS (WGS84) used in your old project
        return new GeometryFactory(new PrecisionModel(), 4326);
    }
}
