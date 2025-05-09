package org.example.hellofx.service;

import org.example.hellofx.AppConfig;
import org.example.hellofx.model.Apartment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
//@Transactional
public class ApartmentService {
    @Autowired
    private RestTemplate restTemplate;

    public boolean checkExistsByApartmentName(String s) {
        return restTemplate.getForObject(AppConfig.backendUrl + "/apartment/checkexistsbyapartmentname?name={name}", Boolean.class, s);
    }

    public Apartment save(Apartment apartment) {
        return restTemplate.postForObject(AppConfig.backendUrl + "/apartment/save", apartment, Apartment.class);
    }

    public Apartment getByApartmentId(Integer apartmentId) {
        return restTemplate.getForObject(AppConfig.backendUrl + "/apartment/getbyapartmentid?id={id}", Apartment.class, apartmentId);
    }

    public List<Integer> getApartmentIdsByBillId(Integer id) {
        return restTemplate.getForObject(AppConfig.backendUrl + "/apartment/getapartmentidsbybillid?id={id}", List.class, id);
    }

    public Apartment findApartmentByApartmentName(String name) {
        return restTemplate.getForObject(AppConfig.backendUrl + "/apartment/findapartmentbyapartmentname?name={name}", Apartment.class, name);
    }

    public void deleteApartmentById(Integer id) {
        restTemplate.delete(AppConfig.backendUrl + "/apartment/deleteapartmentbyid?id={id}", id);
    }
}
