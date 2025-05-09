package org.example.hellofx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.hellofx.dto.ApartmentCountDTO;
import org.example.hellofx.model.*;
import org.example.hellofx.model.enums.AccountType;
import org.example.hellofx.service.*;
import org.example.hellofx.ui.JavaFxApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BillInformationController{
    @Autowired
    private ProfileController profileController;

    @Autowired
    private BillService billService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ResidentService residentService;
    @Autowired
    private SettlementService settlementService;
    @Autowired
    private ApartmentService apartmentService;

    public Resident getResident() {
        return profileController.getResident();
    }

    public Account getProfile() {
        return profileController.getProfile();
    }

    public void saveButtonClicked(Bill oldBill, Bill bill, List<Integer> dsIn, List<Integer> dsOut) {
        if (!bill.equals(oldBill)) {
            billService.updateBill(bill);
        }
        Integer billId = bill.getBillId();
        List<Payment> payments = dsIn.stream()
                .map(d -> new Payment(new Bill(billId), new Apartment(d), null, null))  // Create a Payment object
                .collect(Collectors.toList());

        for (Payment p : payments) {
            p.setPaymentId(null); // Ensure new inserts
        }
        if (!payments.isEmpty()) {
            paymentService.saveAllPayments(payments);
        }
        if (!dsOut.isEmpty()) {
            paymentService.deletePayments(dsOut);
        }
    }

    public void reset(Bill bill) {
        JavaFxApplication.showBillInformationScene(bill.getBillId());
    }

    public ObservableList<Resident> residentQuery(String query) {
        return FXCollections.observableArrayList(residentService.nativeResidentQuery(query));
    }

    public Bill findBillByBillId(Integer billId) {
        return billService.findBillByBillId(billId);
    }

    public ObservableList<String> getAllHouseIds(){
        return FXCollections.observableArrayList(residentService.findDistinctNonNullHouseId(getProfile(), getResident()));
    }

    public List<Payment> getPayments(Integer billId) {
        return paymentService.findPaymentByBillId(billId);
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

    public ObservableList<Integer> getApartmentIdsByBillId(Integer billId) {
        return FXCollections.observableArrayList(apartmentService.getApartmentIdsByBillId(billId));
    }
}
