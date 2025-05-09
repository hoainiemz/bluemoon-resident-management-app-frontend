package org.example.hellofx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.hellofx.dto.VehicleInfo;
import org.example.hellofx.model.Account;
import org.example.hellofx.model.Resident;
import org.example.hellofx.model.Validation;
import org.example.hellofx.model.Vehicle;
import org.example.hellofx.model.enums.AccountType;
import org.example.hellofx.model.enums.VehicleType;
import org.example.hellofx.service.ApartmentService;
import org.example.hellofx.service.ResidentService;
import org.example.hellofx.service.VehicleService;
import org.example.hellofx.ui.JavaFxApplication;
import org.example.hellofx.ui.theme.defaulttheme.VehicleScene;
import org.example.hellofx.validator.VehicleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VehicleController {
    @Autowired
    private ProfileController profileController;
    @Autowired
    private ResidentService residentService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    VehicleValidator vehicleValidator;
    @Autowired
    private ApartmentService apartmentService;

    public Account getProfile() {
        return profileController.getProfile();
    }

    public Resident getResident() {
        return profileController.getResident();
    }

    public ObservableList<String> getAllApartmentName() {
        return FXCollections.observableArrayList(residentService.findDistinctNonNullHouseId(getProfile(), getResident()));
    }

    public ObservableList<VehicleInfo> getVehicleInfoByFilter(String houseIdFilter, VehicleType typeFilter, String searchFilter) {
        if (getProfile().getRole() != AccountType.Resident) {
            return FXCollections.observableArrayList(vehicleService.getVehicleInfoByFilters(houseIdFilter, typeFilter, searchFilter));
        }
        return FXCollections.observableArrayList(vehicleService.getVehicleInfoByResidentAndFilters(getResident().getResidentId(), houseIdFilter, typeFilter, searchFilter));
    }

    public Validation roomCheck(String room, List<String> rooms) {
        return vehicleValidator.roomCheck(room, rooms);
    }

    public Validation licensePlateCheck(String licensePlate) {
        return vehicleValidator.licensePlateCheck(licensePlate);
    }

    public void save(String room, VehicleType type, String licensePlate) {
        Integer roomId = apartmentService.findApartmentByApartmentName(room).getApartmentId();
        Vehicle newve = new Vehicle(roomId, licensePlate, type);
        vehicleService.save(newve);
    }

    public void reset() {
        JavaFxApplication.showThemeScene(VehicleScene.class);
    }

    public void deleteVehicleByVehicleId(Integer id) {
        vehicleService.deleteVehicleById(id);
    }
}
