package org.example.hellofx.service;

import org.example.hellofx.AppConfig;
import org.example.hellofx.dto.VehicleInfo;
import org.example.hellofx.model.Vehicle;
import org.example.hellofx.model.enums.VehicleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
//@Transactional
public class VehicleService {
    @Autowired
    private RestTemplate restTemplate;

    public List<VehicleInfo> getVehicleInfoByFilters(String houseIdFilter, VehicleType typeFilter, String searchFilter) {
        ResponseEntity<List<VehicleInfo>> response = restTemplate.exchange(
                AppConfig.backendUrl + "/vehicle/getvehicleinfobyfilters?houseIdFilter={houseIdFilter}&typeFilter={typeFilter}&searchFilter={searchFilter}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<VehicleInfo>>() {
                },
                houseIdFilter,
                typeFilter,
                searchFilter
        );
        return response.getBody();
    }

    public List<VehicleInfo> getVehicleInfoByResidentAndFilters(Integer residentId, String houseIdFilter, VehicleType typeFilter, String searchFilter) {
        ResponseEntity<List<VehicleInfo>> response = restTemplate.exchange(
                AppConfig.backendUrl + "/vehicle/getvehicleinfobyresidentandfilters?residentId={residentId}&houseIdFilter={houseIdFilter}&typeFilter={typeFilter}&searchFilter={searchFilter}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<VehicleInfo>>() {},
                residentId,
                houseIdFilter,
                typeFilter,
                searchFilter
        );
        return response.getBody();
    }

    public Vehicle save(Vehicle vehicle) {
        return restTemplate.postForObject(AppConfig.backendUrl + "/vehicle/save", vehicle, Vehicle.class);
    }

    public boolean checkExistByLicensePlate(String val) {
        return restTemplate.getForObject(AppConfig.backendUrl + "/vehicle/checkexistbylicenseplate?val={val}", Boolean.class, val);
    }

    public void deleteVehicleById(Integer id) {
        restTemplate.delete(AppConfig.backendUrl + "/vehicle/deletevehiclebyid?id={id}", id);
    }
}
