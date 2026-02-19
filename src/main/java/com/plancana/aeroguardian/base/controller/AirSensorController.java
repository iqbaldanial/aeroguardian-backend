package com.plancana.aeroguardian.base.controller;

import com.plancana.aeroguardian.base.dto.AlertDTO;
import com.plancana.aeroguardian.base.dto.WaqiMapDTO;
import com.plancana.aeroguardian.base.dto.ZoneDTO;
import com.plancana.aeroguardian.base.entity.Alert;
import com.plancana.aeroguardian.base.entity.VulnerableZone;
import com.plancana.aeroguardian.base.repository.AlertRepository;
import com.plancana.aeroguardian.base.repository.VurnerableZoneRepository;
import com.plancana.aeroguardian.base.producer.WaqiService;
import com.plancana.aeroguardian.base.service.AlertService;
import com.plancana.aeroguardian.base.service.GeoJsonImportService;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sensors")
public class AirSensorController {

    @Autowired
    private GeometryFactory factory;
    @Autowired
    private AlertService alertService;
    @Autowired
    private GeoJsonImportService geoJsonImportService;
    @Autowired
    private WaqiService waqiService;
    @Autowired
    private VurnerableZoneRepository vurnerableZoneRepository;
    @Autowired
    private AlertRepository alertRepository;




//    @PostMapping("/reading")
//    public ResponseEntity<String> receiveReading(@Valid @RequestBody AirSensorRequest request){
//        AirSensor sensor = new AirSensor();
//        sensor.setSensorCode(request.sensorCode());
//        sensor.setAqiLevel(request.aqiLevel());
//        // Build the Point from lat/lng in the record
//        Point point = factory.createPoint(new Coordinate(request.longitude(), request.latitude()));
//        sensor.setLocation(point);
//        alertService.processSensorsReading(sensor);
//        return ResponseEntity.ok("Reading received and spatial analysis triggered.");
//    }

    @PostMapping("/import")
    public ResponseEntity<String> importZones() throws IOException {
        geoJsonImportService.importZones();

        return ResponseEntity.ok("Able to read the json file");
    }

    @GetMapping("/sync")
    public ResponseEntity<WaqiMapDTO> syncAeroGuardian() {
        try {
            WaqiMapDTO data = waqiService.fetchGridData();
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .build();
        }
    }

    @GetMapping("/zones")
    public ResponseEntity<List<ZoneDTO>> getAllZones() {
        try {
            List<VulnerableZone> zones = geoJsonImportService.getAllZones();
            List<ZoneDTO> dtos = zones.stream().map(zone -> new ZoneDTO(
                    zone.getId(),
                    zone.getName(),
                    zone.getZoneType(),
                    zone.getArea().toText() // Converts Polygon to "POLYGON((...))" String
            )).toList();
            return dtos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<AlertDTO>> getAllAlerts(){
        try {
            List<Alert> alerts = alertRepository.findAllByOrderByTimestampDesc();
            List<AlertDTO> dtos = alerts.stream().map(alert -> new AlertDTO(
//                    alert.getZone(),
                    alert.getStationName(),
                    alert.getAqiValue(),
                    alert.getZoneName(),
                    alert.getTimestamp(),
                    alert.getAqiLevel()
            )).toList();
            return dtos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(dtos);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/alerts/count-by-station")
    public ResponseEntity<List<AlertRepository.StationAlertCount>> countAlertsByStation() {
        try {
            List<AlertRepository.StationAlertCount> counts = alertRepository.countByStationName();
            return counts.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(counts);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
