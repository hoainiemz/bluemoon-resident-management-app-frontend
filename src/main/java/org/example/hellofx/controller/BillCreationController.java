package org.example.hellofx.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.hellofx.dto.ApartmentCountDTO;
import org.example.hellofx.dto.BillSchedulerDTO;
import org.example.hellofx.model.*;
import org.example.hellofx.model.enums.AccountType;
import org.example.hellofx.service.*;
import org.example.hellofx.ui.JavaFxApplication;
import org.example.hellofx.ui.theme.defaulttheme.BillCreationScene;
import org.example.hellofx.validator.SchedulerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//import static java.util.stream.Nodes.collect;

@Component
public class BillCreationController{
    @Autowired
    private ProfileController profileController;

    @Autowired
    private BillService billService;
    @Autowired
    private ResidentService residentService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private SettlementService settlementService;
    @Autowired
    private SchedulerValidator schedulerValidator;
    @Autowired
    private SchedulerService schedulerService;

    public Account getProfile() {
        return profileController.getProfile();
    }

    public Resident getResident() {
        return profileController.getResident();
    }


//    @Transactional
    public void createButtonClicked(Bill bill, List<Integer> apartmentIds) {
        bill = billService.save(bill);
        Integer billId = bill.getBillId();
        List<Payment> payments = apartmentIds.stream()
                .map(d -> new Payment(new Bill(billId), new Apartment(d), null, null))  // Create a Payment object
                .collect(Collectors.toList());

        for (Payment p : payments) {
            p.setPaymentId(null); // Ensure new inserts
        }
        paymentService.saveAllPayments(payments);
    }
    public void createButtonClicked(Bill bill, List<Integer> apartmentIds, LocalDateTime time, String cycle) {
        bill.setDueDate(time);
        BillSchedulerDTO schedulerDTO = new BillSchedulerDTO(bill, apartmentIds);
        String json = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {json = objectMapper.writeValueAsString(schedulerDTO);
//            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Scheduler scheduler = new Scheduler("Bill", json, time, cycle);
        schedulerService.save(scheduler);
    }

    public void reset() {
        JavaFxApplication.showThemeScene(BillCreationScene.class);
    }

    public ObservableList<Resident> residentQuery(String query) {
        return FXCollections.observableArrayList(residentService.nativeResidentQuery(query));
    }

    public ObservableList<String> getAllHouseIds(){
        return FXCollections.observableArrayList(residentService.findDistinctNonNullHouseId(getProfile(), getResident()));
    }

    public ObservableList<Resident> getResidentsByFilters(String houseNameFilter, String roleFilter, String searchFilter) {
        return FXCollections.observableArrayList(residentService.findResidentsByFilters(houseNameFilter, roleFilter, searchFilter));
    }

    public ObservableList<ApartmentCountDTO> getApartmentsAndResidentCount(String s) {
        if (getProfile().getRole() == AccountType.Resident) {
            return FXCollections.observableArrayList(settlementService.getApartmentsAndResidentCount(getResident().getResidentId(), s));
        }
        return FXCollections.observableArrayList(settlementService.getApartmentsAndResidentCountBySearch(s));
    }

    public Validation scheduleValidate(LocalDateTime time, String cycle) {
        return schedulerValidator.validate(time, cycle);
    }

}
