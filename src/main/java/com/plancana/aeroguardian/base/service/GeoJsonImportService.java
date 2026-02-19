package com.plancana.aeroguardian.base.service;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.plancana.aeroguardian.base.dto.GeoJsonDTO;
import com.plancana.aeroguardian.base.dto.WaqiMapDTO;
import com.plancana.aeroguardian.base.entity.VulnerableZone;
import com.plancana.aeroguardian.base.repository.VurnerableZoneRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.locationtech.jts.geom.Polygon;
import org.n52.jackson.datatype.jts.JtsModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@Service
public class GeoJsonImportService {

    @Autowired
    private VurnerableZoneRepository vurnerableZoneRepository;
    @Autowired
    private ObjectMapper objectMapper;


    @Transactional
    public void importZones() throws IOException {
        try {
            InputStream is = new ClassPathResource("data/KualaLumpur_Facilities.json").getInputStream();
            GeoJsonDTO.GeoJsonCollection collections = objectMapper.readValue(is, GeoJsonDTO.GeoJsonCollection.class);

            for(var feature : collections.features()){
                if(feature.geometry() instanceof Polygon polygon){
                    VulnerableZone zone = new VulnerableZone();
                    String name = (String) feature.properties().getOrDefault("name", "Unknown Facility");
                    String type = (String) feature.properties().getOrDefault("amenity", "facility");

                    zone.setName(name);
                    zone.setZoneType(type.toUpperCase());
                    zone.setArea(polygon);
                    vurnerableZoneRepository.save(zone);

                }
            }
            System.out.println("✅ Bulk Import Complete: " + collections.features().size() + " facilities processed.");
        } catch (Exception e) {
            System.err.println("❌ Import Failed: " + e.getMessage());
        }
    }

    public List<VulnerableZone> getAllZones() {
        return vurnerableZoneRepository.findAll();
    }

}
