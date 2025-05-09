package org.example.hellofx.dto;

import org.example.hellofx.model.Account;
import org.example.hellofx.model.Resident;

public class AccountResidentWrapper {
    private Account profile;
    private Resident resident;

    public Account getProfile() {
        return profile;
    }

    public void setProfile(Account profile) {
        this.profile = profile;
    }

    public Resident getResident() {
        return resident;
    }

    public void setResident(Resident resident) {
        this.resident = resident;
    }

    public AccountResidentWrapper(Account profile, Resident resident) {
        this.profile = profile;
        this.resident = resident;
    }
}
