package com.plancana.aeroguardian.base.service;

import com.plancana.aeroguardian.base.entity.AirSensor;
import com.plancana.aeroguardian.base.entity.VulnerableZone;
import com.plancana.aeroguardian.base.repository.AirSensorRepository;
import com.plancana.aeroguardian.base.repository.AlertRepository;
import com.plancana.aeroguardian.base.repository.VurnerableZoneRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class AlertService {
    @Autowired
    private AlertRepository alertRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(AlertService.class);

    // Runs every day at midnight (Cron: sec min hour day month weekday)
    @Scheduled(cron = "0 0 0 * * *")
    public void performTtlCleanup(){
        //  retention period 30 days
        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);

        LOGGER.info("Starting TTL Cleanup: Removing alerts older than {}", cutoff);
        try {
            alertRepository.deleteByTimestampBefore(cutoff);
            LOGGER.info("Cleanup completed");
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
    }



}
