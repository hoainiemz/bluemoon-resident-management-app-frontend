package org.example.hellofx.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.hellofx.dto.ApartmentCountDTO;
import org.example.hellofx.dto.BillSchedulerDTO;
import org.example.hellofx.model.Account;
import org.example.hellofx.model.Bill;
import org.example.hellofx.model.Resident;
import org.example.hellofx.model.Scheduler;
import org.example.hellofx.model.enums.AccountType;
import org.example.hellofx.service.ApartmentService;
import org.example.hellofx.service.SchedulerService;
import org.example.hellofx.service.SettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BillSchedulerInformationController {
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private SettlementService settlementService;
    @Autowired
    private ProfileController profileController;
    @Autowired
    private ApartmentService apartmentService;

    public Scheduler findById(Integer id) {
        return schedulerService.getById(id);
    }

    public Account getProfile() {
        return profileController.getProfile();
    }

    public Resident getResident() {
        return profileController.getResident();
    }

    public ObservableList<ApartmentCountDTO> getApartmentsAndResidentCount(String s) {
        if (getProfile().getRole() == AccountType.Resident) {
            return FXCollections.observableArrayList(settlementService.getApartmentsAndResidentCount(getResident().getResidentId(), s));
        }
        return FXCollections.observableArrayList(settlementService.getApartmentsAndResidentCountBySearch(s));
    }

    public void saveButtonClicked(int id, Bill bill, List<Integer> ds, String cycle) {
        BillSchedulerDTO schedulerDTO = new BillSchedulerDTO(bill, ds);
        schedulerDTO.setId(id);
        String json = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {json = objectMapper.writeValueAsString(schedulerDTO);
//            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Scheduler scheduler = new Scheduler("Bill", json, bill.getDueDate(), cycle);
        scheduler.setSchedulerId(Long.valueOf(id));
        schedulerService.save(scheduler);
    }
}
