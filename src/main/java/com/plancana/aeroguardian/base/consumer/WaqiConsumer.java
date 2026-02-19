package com.plancana.aeroguardian.base.consumer;

import com.plancana.aeroguardian.base.dto.WaqiMapDTO;
import com.plancana.aeroguardian.base.entity.Alert;
import com.plancana.aeroguardian.base.entity.VulnerableZone;
import com.plancana.aeroguardian.base.enums.AqiLevel;
import com.plancana.aeroguardian.base.repository.AlertRepository;
import com.plancana.aeroguardian.base.repository.VurnerableZoneRepository;
import com.plancana.aeroguardian.base.service.EmailService;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class WaqiConsumer {


    @Autowired
    private VurnerableZoneRepository vurnerableZoneRepository;
    @Autowired
    private AlertRepository alertRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private GeometryFactory factory;

    // This method "wakes up" automatically when a message hits the queue
    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    private void processStationReading(WaqiMapDTO.Station station) {
        if (station.aqi().equals("-")) return;

        double aqiValue = Double.parseDouble(station.aqi());
        AqiLevel level = AqiLevel.fromAQI(aqiValue);
        // Use JTS to create a point from the station's coordinates

        if(aqiValue >50){
            Point stationPoint = factory.createPoint(new Coordinate(station.lon(), station.lat()));

            List<VulnerableZone> affectedZones = vurnerableZoneRepository.findZonesNearPoint(stationPoint, 500.0);
            for (VulnerableZone zone : affectedZones) {
                // Threshold for "Unhealthy"
//                System.out.println("🚨 IMPACT ALERT: " + zone.getName() +
//                        " is currently exposed to AQI: " + aqiValue +
//                        " (Source: " + station.station().name() + ")");
                Alert newAlert = Alert.builder()
                        .zone(zone)
                        .stationName(station.station().name())
                        .aqiValue(aqiValue)
                        .timestamp(LocalDateTime.now())
                        .aqiLevel(level)
                        .zoneName(zone.getName())
                        .build();

                alertRepository.save(newAlert);
                System.out.println("Persistent Alert Saved for: " + zone.getName());

                // email when the aqi is above 100
                if(aqiValue >100){
                    emailService.sendEmail("iqbaldanialfirdaus@gmail.com",zone.getName(),level,aqiValue);
                }

            }

        }

    }
}
