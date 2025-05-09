package org.example.hellofx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.hellofx.model.*;
import org.example.hellofx.service.ApartmentService;
import org.example.hellofx.service.ResidentService;
import org.example.hellofx.service.SettlementService;
import org.example.hellofx.ui.JavaFxApplication;
import org.example.hellofx.ui.theme.defaulttheme.ApartmentInformationScene;
import org.example.hellofx.validator.ApartmentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApartmentInformationController {
    @Autowired
    private ProfileController profileController;
    @Autowired
    private ApartmentService apartmentService;
    @Autowired
    private ApartmentValidator apartmentValidator;
    @Autowired
    private ResidentService residentService;

    private Integer apartmentId;
    private Apartment apartment;
    @Autowired
    private SettlementService settlementService;

    public Account getProfile() {
        return profileController.getProfile();
    }

    public Resident getResident() {
        return profileController.getResident();
    }

    public void setApartmentId(Integer apartmentId) {
        this.apartmentId = apartmentId;
    }

    public Apartment getApartment() {
        apartment = apartmentService.getByApartmentId(apartmentId);
        return apartment;
    }

    public Validation checkName(String name) {
        return apartmentValidator.checkName(name);
    }

    public void reset() {
        JavaFxApplication.showThemeScene(ApartmentInformationScene.class);
    }

    public void save(String name, BigDecimal monthlyRentPrice, Integer lastMonthElectricIndex, BigDecimal electricUnitPrice, Integer lastMonthWaterIndex, BigDecimal waterUnitPrice, List<Integer> dsIn, List<Integer> dsOut) {
        Apartment newapartment = new Apartment(name, monthlyRentPrice, lastMonthElectricIndex, electricUnitPrice, lastMonthWaterIndex, waterUnitPrice);
        newapartment.setApartmentId(apartmentId);
        apartmentService.save(newapartment);

        List<Settlement> settlements = dsIn.stream()
                .map(d -> new Settlement(d, newapartment.getApartmentId()))  // Create a Payment object
                .collect(Collectors.toList());
        for(Settlement s : settlements) {
            s.setSettlementId(null);
        }
        settlementService.saveAll(settlements);

        settlementService.deleteByIds(dsOut);
    }

    public ObservableList<Resident> residentQuery(String query) {
        return FXCollections.observableArrayList(residentService.nativeResidentQuery(query));
    }

    public List<Settlement> getSettlementsByApartmentId(Integer id) {
        return settlementService.getSettlementsByApartmentId(id);
    }

    public ObservableList<Resident> getResidentsByFilters(String houseNameFilter, String roleFilter, String searchFilter) {
        return FXCollections.observableArrayList(residentService.findResidentsByFilters(houseNameFilter, roleFilter, searchFilter));
    }
}
