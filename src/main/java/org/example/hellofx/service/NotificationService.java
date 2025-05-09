package org.example.hellofx.service;

import org.example.hellofx.AppConfig;
import org.example.hellofx.model.NotificationItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
//@Transactional
public class NotificationService {
    @Autowired
    private RestTemplate restTemplate;

    public NotificationItem findById(Integer id) {
        return restTemplate.getForObject(AppConfig.backendUrl + "/notification/findbyid?id={id}", NotificationItem.class, id);
    }

    public List<NotificationItem> findNotifications(String typeFilter, String searchFilter) {
        ResponseEntity<List<NotificationItem>> response = restTemplate.exchange(
                AppConfig.backendUrl + "/notification/findnotifications?typeFilter={typeFilter}&searchFilter={searchFilter}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<NotificationItem>>() {},
                typeFilter,
                searchFilter
        );
        return response.getBody();
    }

    public List<NotificationItem> findTopByResidentIdAndWatchedStatusOrderByCreatedAtDesc(Integer residentId, Boolean unReadOnly) {

        ResponseEntity<List<NotificationItem>> response = restTemplate.exchange(
                AppConfig.backendUrl + "/notification/findtopbyresidentidandwatchedstatusorderbycreatedatdesc?residentId={residentId}&unReadOnly={unReadOnly}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<NotificationItem>>() {},
                residentId,
                unReadOnly
        );
        return response.getBody();
    }

    public NotificationItem save(NotificationItem notificationItem) {
        return restTemplate.postForObject(AppConfig.backendUrl + "/notification/save", notificationItem, NotificationItem.class);
    }

    public void deleteNotificationById(Integer id) {
        restTemplate.delete(AppConfig.backendUrl + "/notification/deletenotificationbyid?id={id}", id);
    }

    public List<NotificationItem> findAll() {
        return restTemplate.getForObject(AppConfig.backendUrl + "/notification/findall", List.class);
    }
}
