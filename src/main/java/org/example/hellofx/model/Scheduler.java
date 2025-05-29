package org.example.hellofx.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.hellofx.dto.BillSchedulerDTO;
import org.example.hellofx.dto.NotificationSchedulerDTO;

import java.time.LocalDateTime;

public class Scheduler {

    private Long schedulerId;

    private String schedulerType;

    private String content; // Store JSON as a String in a jsonb column

    private LocalDateTime nextExecution;

    private String cycle; // Example values: "hour", "day", etc.

    // Getters and Setters
    public Scheduler() {
    }

    public Scheduler(Long schedulerId, String schedulerType, String content, LocalDateTime nextExecution, String cycle) {
        this.schedulerId = schedulerId;
        this.schedulerType = schedulerType;
        this.content = content;
        this.nextExecution = nextExecution;
        this.cycle = cycle;
    }

    public Scheduler(String schedulerType, String content, LocalDateTime nextExecution, String cycle) {
        this.schedulerType = schedulerType;
        this.content = content;
        this.nextExecution = nextExecution;
        this.cycle = cycle;
    }

    public Long getSchedulerId() { return schedulerId; }
    public void setSchedulerId(Long schedulerId) { this.schedulerId = schedulerId; }

    public String getSchedulerType() { return schedulerType; }
    public void setSchedulerType(String schedulerType) { this.schedulerType = schedulerType; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getNextExecution() { return nextExecution; }
    public void setNextExecution(LocalDateTime nextExecution) { this.nextExecution = nextExecution; }

    public String getCycle() { return cycle; }
    public void setCycle(String cycle) { this.cycle = cycle; }


    public BillSchedulerDTO billDTO() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            // Parse the JSON content to extract Bill and apartmentIds
            BillSchedulerDTO data = objectMapper.readValue(this.content, BillSchedulerDTO.class);

            BillSchedulerDTO dto = new BillSchedulerDTO();
            dto.setId(this.schedulerId.intValue());
            dto.setBill(data.getBill());
            dto.setApartmentIds(data.getApartmentIds());

            return dto;
        } catch (JsonProcessingException e) {
            // Handle JSON parsing error
            e.printStackTrace();
            return null;
        }
    }

    public NotificationSchedulerDTO notificationDTO() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            // Parse the JSON content to extract Bill and apartmentIds
            NotificationSchedulerDTO data = objectMapper.readValue(this.content, NotificationSchedulerDTO.class);

            NotificationSchedulerDTO dto = new NotificationSchedulerDTO();
            dto.setId(this.schedulerId.intValue());
            dto.setNotificationItem(data.getNotificationItem());
            dto.setResidentIds(data.getResidentIds());

            return dto;
        } catch (JsonProcessingException e) {
            // Handle JSON parsing error
            e.printStackTrace();
            return null;
        }
    }
}
