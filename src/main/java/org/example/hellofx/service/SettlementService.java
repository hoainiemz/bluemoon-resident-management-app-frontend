package org.example.hellofx.service;

import org.example.hellofx.AppConfig;
import org.example.hellofx.dto.ApartmentCountDTO;
import org.example.hellofx.model.Settlement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class SettlementService {
    @Autowired
    private RestTemplate restTemplate;

    public List<ApartmentCountDTO> getApartmentsAndResidentCount(Integer residentId, String s) {
        ResponseEntity<List<ApartmentCountDTO>> response = restTemplate.exchange(
                AppConfig.backendUrl + "/settlement/getapartmentsandresidentcount?residentId={residentId}&s={s}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ApartmentCountDTO>>() {},
                residentId,
                s
        );
        return response.getBody();
    }

    public List<ApartmentCountDTO> getApartmentsAndResidentCountBySearch(String s) {
        ResponseEntity<List<ApartmentCountDTO>> response = restTemplate.exchange(
                AppConfig.backendUrl + "/settlement/getapartmentsandresidentcountbysearch?s={s}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ApartmentCountDTO>>() {},
                s
        );
        return response.getBody();
//        return restTemplate.getForObject(AppConfig.backendUrl + "/settlement/getapartmentsandresidentcountbysearch?s={s}", List.class, s);
    }

    public List<Settlement> getSettlementsByApartmentId(Integer id) {
        ResponseEntity<List<Settlement>> response = restTemplate.exchange(
                AppConfig.backendUrl + "/settlement/getsettlementsbyapartmentid?id={id}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Settlement>>() {},
                id
        );
        return response.getBody();
    }

    public void saveAll(List<Settlement>ds) {
        restTemplate.postForObject(AppConfig.backendUrl + "/settlement/saveall", ds, Void.class);
    }

    public void deleteByIds(List<Integer> ds) {
        restTemplate.postForObject(AppConfig.backendUrl + "/settlement/deletebyids", ds, Void.class);
    }
}
