package com.plancana.aeroguardian.base.repository;

import com.plancana.aeroguardian.base.entity.VulnerableZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface VurnerableZoneRepository extends JpaRepository<VulnerableZone, UUID> {


    @Query(value = "SELECT * FROM vulnerable_zones vz" +
            " WHERE ST_DWithin(vz.area::geography, :point, :radiusInMeters) = true",
            nativeQuery = true)
    List<VulnerableZone> findZonesNearPoint(
            @Param("point") org.locationtech.jts.geom.Point point,
            @Param("radiusInMeters") double radiusInMeters
            );

}
