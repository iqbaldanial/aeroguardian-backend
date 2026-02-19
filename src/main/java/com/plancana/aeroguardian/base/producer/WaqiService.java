package com.plancana.aeroguardian.base.producer;

import com.plancana.aeroguardian.base.dto.WaqiDTO;
import com.plancana.aeroguardian.base.dto.WaqiMapDTO;
import com.plancana.aeroguardian.base.entity.AirSensor;
import com.plancana.aeroguardian.base.repository.VurnerableZoneRepository;
import com.plancana.aeroguardian.base.service.AlertService;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WaqiService {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.queue.name}")
    private  String queue;

    @Value("${rabbitmq.routingkey.name}")
    private  String routing_key;

    @Value("${waqi.api.token}")
    private String aqiApiToken;

    @Autowired
    private AlertService alertService;

    @Autowired
    private GeometryFactory factory;

    @Autowired
    private VurnerableZoneRepository vurnerableZoneRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final Logger LOGGER = LoggerFactory.getLogger(WaqiService.class);

    public WaqiMapDTO fetchGridData(){
        String bounds = "2.95,101.50,3.30,101.80"; // in kl 2.95,101.50,3.30,101.80 in malaysia 1.20,99.60,6.80,104.50
        String url = "https://api.waqi.info/map/bounds?token=" + aqiApiToken + "&latlng=" + bounds;

        try {
            WaqiMapDTO response = restTemplate.getForObject(url, WaqiMapDTO.class);

            if(response != null & "ok".equals(response.status())){
                for(var station : response.data()){
//                    processStationReading(station);
                    rabbitTemplate.convertAndSend(exchange,routing_key,station);
                }
                LOGGER.info("Json message sent -> {}", response);
                return response;
            }
        } catch (RuntimeException e) {
            System.err.println("Grid Fetch Failed: " + e.getMessage());
            throw new RuntimeException("Failed to fetch air quality grid: " + e.getMessage());
        }
        return null;
    }

    @Scheduled(fixedRate = 900000)  // fetches every 15 mins
    public void autoFetchGridData (){
        LOGGER.info("Scheduled Task: Starting automatic AQI grid sync...");
        fetchGridData();
    }


//    @Scheduled(fixedRate = 180000)
//    public void fetchRealTimeData(){
//        String url = "https://api.waqi.info/feed/petaling-jaya/?token=" + aqiApiToken;
//
//        try {
//            String raw = restTemplate.getForObject(url, String.class);
//            System.out.println("raw" + raw);
//            WaqiDTO response = restTemplate.getForObject(url, WaqiDTO.class);
//            var data = response.data();
//            int liveAqi = data.aqi();
//            double lat = data.city().coordinates().get(0);
//            double lng = data.city().coordinates().get(1);
//
//            AirSensor airSensor = new AirSensor();
//            airSensor.setSensorCode("WAQI-KL-STATION");
//            airSensor.setAqiLevel(Double.valueOf(liveAqi));
//            airSensor.setLocation(factory.createPoint(new Coordinate(lng, lat)));
//            alertService.processSensorsReading(airSensor);
//
//            System.out.println("Live Data Sync: AQI " + liveAqi + " fetched for KL.");
//        } catch (Exception e) {
//            System.err.println("Failed to fetch live AQI data: " + e.getMessage());
//        }
//    }

// move to consumer
//    private void processStationReading(WaqiMapDTO.Station station) {
//        if(station.aqi().equals("-")) return;
//
//        double aqiValue = Double.parseDouble(station.aqi());
//        // Use JTS to create a point from the station's coordinates
//        Point stationPoint = factory.createPoint(new Coordinate(station.lon(),station.lat()));
//
//        List<VulnerableZone> affectedZones = vurnerableZoneRepository.findZonesNearPoint(stationPoint,500.0);
//        for (VulnerableZone zone : affectedZones) {
//            if (aqiValue > 40) { // Threshold for "Unhealthy"
//                System.out.println("🚨 IMPACT ALERT: " + zone.getName() +
//                        " is currently exposed to AQI: " + aqiValue +
//                        " (Source: " + station.station().name() + ")");
//
//                // Here is where you will later call the Notification logic
//            }
//        }
//
//    }


}
