package org.example.hellofx.service;

import org.example.hellofx.AppConfig;
import org.example.hellofx.dto.BillSchedulerDTO;
import org.example.hellofx.dto.PaymentProjectionDTO;
import org.example.hellofx.model.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class SchedulerService {
    @Autowired
    RestTemplate restTemplate;

    public void save(Scheduler scheduler) {
        restTemplate.postForObject(AppConfig.backendUrl + "/scheduler/save", scheduler, Void.class);
    }

    public List<Scheduler> getBillByFilter(int requireFilter, String searchFilter) {
        ResponseEntity<List<Scheduler>> response = restTemplate.exchange(
                AppConfig.backendUrl + "/scheduler/getbillbyfilter?requireFilter={requireFilter}&searchFilter={searchFilter}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Scheduler>>() {},
                requireFilter,
                searchFilter
        );
        return response.getBody();
    }
    public List<Scheduler> getNotificationByFilter(String typeFilter, String searchFilter) {
        ResponseEntity<List<Scheduler>> response = restTemplate.exchange(
                AppConfig.backendUrl + "/scheduler/getnotificationbyfilter?typeFilter={typeFilter}&searchFilter={searchFilter}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Scheduler>>() {},
                typeFilter,
                searchFilter
        );
        return response.getBody();
    }

    public Scheduler getById(int id) {
        ResponseEntity<Scheduler> response = restTemplate.exchange(
                AppConfig.backendUrl + "/scheduler/getbyid?id={id}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Scheduler>() {},
                id
        );
        return response.getBody();
    }

    public void deleteById(int id) {
        String url = AppConfig.backendUrl + "/scheduler/delete?id=" + id;
        restTemplate.delete(url);
    }
}
