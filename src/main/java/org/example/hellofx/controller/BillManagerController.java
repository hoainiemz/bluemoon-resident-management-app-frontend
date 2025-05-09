package org.example.hellofx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.hellofx.SpringBootFxApplication;
import org.example.hellofx.model.Account;
import org.example.hellofx.model.Bill;
import org.example.hellofx.model.Resident;
import org.example.hellofx.service.BillService;
import org.example.hellofx.service.PaymentService;
import org.example.hellofx.ui.JavaFxApplication;
import org.example.hellofx.ui.theme.defaulttheme.BillCreationScene;
import org.example.hellofx.ui.theme.defaulttheme.BillInformationScene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class BillManagerController{
    @Autowired
    private ProfileController profileController;

    @Autowired
    private BillService billService;
    @Autowired
    private PaymentService paymentService;

    public Resident getResident() {
        return profileController.getResident();
    }

    public Account getProfile() {
        return profileController.getProfile();
    }

    public void seeBillInformation(Integer billId) {
        JavaFxApplication.showBillInformationScene(billId);
    }

    public ObservableList<Bill> getBills(int requireFilter, int dueFilter, String searchFilter) {
        return FXCollections.observableArrayList(billService.findBillsByFilters(requireFilter, dueFilter, searchFilter));
    }

    public Scene getBillCreationScene(Scene scene) {
        return SpringBootFxApplication.context.getBean(BillCreationScene.class).getScene(scene);
    }

    public Scene getBillInfoScene(Scene scene, Integer billId) {
        BillInformationScene theme = SpringBootFxApplication.context.getBean(BillInformationScene.class);
        return theme.getScene(billId, scene);
    }

    public void generatePaymentsForBill(Bill bill) {
        paymentService.generatePaymentsForBill(bill);
    }

    public Bill saveBill(Bill bill) {
        return billService.save(bill);
    }

    public void deleteBillByBillId(Integer billId) {
        billService.deleteBillById(billId);
    }

    public void export(List<Integer> billIds, Stage stage) throws Exception  {
        paymentService.export(billIds, stage);
    }
}
