package org.example.hellofx.service;

import org.example.hellofx.AppConfig;
import org.example.hellofx.model.Noticement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class NoticementService {
    @Autowired
    private RestTemplate restTemplate;

    public List<Noticement> findAllByNotificationId(Integer id) {
        ResponseEntity<List<Noticement>> response = restTemplate.exchange(
                AppConfig.backendUrl + "/noticement/findallbynotificationid?id={id}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Noticement>>() {},
                id
        );
        return response.getBody();
    }

    public void markAsWatched(Integer notificationId, Integer residentId) {
        restTemplate.postForObject(AppConfig.backendUrl + "/noticement/markaswatched?notificationId={notificationId}&residentId={residentId}", null, Void.class, notificationId, residentId);
    }

    public void saveAll(List<Noticement> noticements) {
        restTemplate.postForObject(AppConfig.backendUrl + "/noticement/saveall", noticements, Void.class);
    }

    public void deleteNoticementsByNoticementId(List<Integer> dsout) {
        restTemplate.postForObject(AppConfig.backendUrl + "/noticement/deletenoticementsbynoticementid", dsout, Void.class);
    }
}
