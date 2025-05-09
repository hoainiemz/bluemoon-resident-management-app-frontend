package org.example.hellofx.service;

import org.example.hellofx.AppConfig;
import org.example.hellofx.model.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class FeedbackService {
    @Autowired
    private RestTemplate restTemplate;

    public Feedback save(Feedback feedback) {
        return restTemplate.postForObject(AppConfig.backendUrl + "/feedback/save", feedback, Feedback.class);
    }

    public List<Feedback> getTopFeedbackByWatchedStatusOrderByCreatedAtDesc(boolean watchedStatus) {
        ResponseEntity<List<Feedback>> response = restTemplate.exchange(
                AppConfig.backendUrl + "/feedback/gettopfeedbackbywatchedstatusorderbycreatedatdesc?watchedStatus={watchedStatus}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Feedback>>() {},
                watchedStatus
        );
        return response.getBody();
    }
}
