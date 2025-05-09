package org.example.hellofx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.hellofx.model.*;
import org.example.hellofx.service.ApartmentService;
import org.example.hellofx.service.ResidentService;
import org.example.hellofx.service.SettlementService;
import org.example.hellofx.ui.JavaFxApplication;
import org.example.hellofx.ui.theme.defaulttheme.ApartmentCreationScene;
import org.example.hellofx.validator.ApartmentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApartmentCreationController {
    @Autowired
    private ProfileController profileController;
    @Autowired
    private ResidentService residentService;
    @Autowired
    private ApartmentValidator apartmentValidator;
    @Autowired
    private ApartmentService apartmentService;
    @Autowired
    private SettlementService settlementService;

    public Account getProfile() {
        return profileController.getProfile();
    }

    public Resident getResident() {
        return profileController.getResident();
    }

    public ObservableList<Resident> residentQuery(String query) {
        return FXCollections.observableArrayList(residentService.nativeResidentQuery(query));
    }

    public ObservableList<String> getAllHouseIds(){
        return FXCollections.observableArrayList(residentService.findDistinctNonNullHouseId(getProfile(), getResident()));
    }

    public void reset() {
        JavaFxApplication.showThemeScene(ApartmentCreationScene.class);
    }

    public Validation checkName(String name) {
        return apartmentValidator.checkName(name);
    }

    public void save(String name, BigDecimal monthlyRentPrice, Integer lastMonthElectricIndex, BigDecimal electricUnitPrice, Integer lastMonthWaterIndex, BigDecimal waterUnitPrice, List<Integer>ds) {
        Apartment apartment = new Apartment(name, monthlyRentPrice, lastMonthElectricIndex, electricUnitPrice, lastMonthWaterIndex, waterUnitPrice);
        apartment.setApartmentId(null);
        Apartment app = apartmentService.save(apartment);
        List<Settlement> settlements = ds.stream()
                .map(d -> new Settlement(d, app.getApartmentId()))  // Create a Payment object
                .collect(Collectors.toList());
        for(Settlement s : settlements) {
            s.setSettlementId(null);
        }

        settlementService.saveAll(settlements);
    }

    public ObservableList<Resident> getResidentsByFilters(String houseNameFilter, String roleFilter, String searchFilter) {
        return FXCollections.observableArrayList(residentService.findResidentsByFilters(houseNameFilter, roleFilter, searchFilter));
    }
}
