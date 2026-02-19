package com.plancana.aeroguardian.base.repository;

import com.plancana.aeroguardian.base.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AlertRepository extends JpaRepository<Alert, UUID> {
    List<Alert> findByZoneIdAndTimestampAfter(UUID zoneid, LocalDateTime timestamp);

    @Transactional
    void deleteByTimestampBefore(LocalDateTime cutoff);

    @Query("select a.stationName as stationName, count(a) as total from Alert a group by a.stationName")
    List<StationAlertCount> countByStationName();

    interface StationAlertCount {
        String getStationName();
        long getTotal();
    }

    List<Alert> findAllByOrderByTimestampDesc();
}
