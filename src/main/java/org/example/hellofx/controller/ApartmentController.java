package org.example.hellofx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import org.example.hellofx.SpringBootFxApplication;
import org.example.hellofx.dto.ApartmentCountDTO;
import org.example.hellofx.model.Account;
import org.example.hellofx.model.Resident;
import org.example.hellofx.model.enums.AccountType;
import org.example.hellofx.service.ApartmentService;
import org.example.hellofx.service.SettlementService;
import org.example.hellofx.ui.theme.defaulttheme.ApartmentCreationScene;
import org.example.hellofx.ui.theme.defaulttheme.ApartmentInformationScene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApartmentController {
    @Autowired
    private ProfileController profileController;
    @Autowired
    private SettlementService settlementService;
    @Autowired
    private ApartmentService apartmentService;

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

    public Scene getApartmentCreationScene(Scene scene) {
        return SpringBootFxApplication.context.getBean(ApartmentCreationScene.class).getScene(scene);
    }
    public Scene getApartmentInformationScene(Scene scene, Integer apartmentId) {
        ApartmentInformationController controller = SpringBootFxApplication.context.getBean(ApartmentInformationController.class);
        controller.setApartmentId(apartmentId);
        ApartmentInformationScene theme = SpringBootFxApplication.context.getBean(ApartmentInformationScene.class);
        return theme.getScene(scene);
//        return JavaFxApplication.showApartmentInformationScene(scene, apartmentId);
    }

    public void deleteApartmentByApartmentId(Integer id) {
        apartmentService.deleteApartmentById(id);
    }
}
