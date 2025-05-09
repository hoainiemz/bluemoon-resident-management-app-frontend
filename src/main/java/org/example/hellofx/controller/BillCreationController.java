package org.example.hellofx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.hellofx.dto.ApartmentCountDTO;
import org.example.hellofx.model.*;
import org.example.hellofx.model.enums.AccountType;
import org.example.hellofx.service.BillService;
import org.example.hellofx.service.PaymentService;
import org.example.hellofx.service.ResidentService;
import org.example.hellofx.service.SettlementService;
import org.example.hellofx.ui.JavaFxApplication;
import org.example.hellofx.ui.theme.defaulttheme.BillCreationScene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;

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
}
