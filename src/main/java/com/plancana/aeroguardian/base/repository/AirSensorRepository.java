package com.plancana.aeroguardian.base.repository;

import com.plancana.aeroguardian.base.entity.AirSensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AirSensorRepository extends JpaRepository<AirSensor,UUID> {

    // OUTSTANDER FEATURE: Find all sensors within a certain distance (meters) of a point
    @Query(value = "SELECT * FROM sensors s WHERE ST_DWithin(s.location, :point, :distance)", nativeQuery = true)
    List<AirSensor> findSensorsNear(@Param("point") org.locationtech.jts.geom.Point point, @Param("distance") double distance);

    // Find sensors with high pollution levels
    List<AirSensor> findByAqiLevelGreaterThan(Double level);
}
