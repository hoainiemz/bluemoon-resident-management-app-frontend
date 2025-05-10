package org.example.hellofx.service;

import org.example.hellofx.AppConfig;
import org.example.hellofx.dto.AccountResidentWrapper;
import org.example.hellofx.model.Account;
import org.example.hellofx.model.Resident;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

//@Transactional
@Service
public class ResidentService {
    @Autowired
    RestTemplate restTemplate;

    public List<Resident> nativeResidentQuery(String query) {
        return Arrays.asList(restTemplate.getForObject(AppConfig.backendUrl + "/resident/nativeresidentquery?query={query}", Resident[].class, query));
    }

    public Resident findResidentByUserId(int id) {
        return restTemplate.getForObject(AppConfig.backendUrl + "/resident/findresidentbyuserid?id={id}", Resident.class, id);
    }

    public boolean checkResidentExistByIdentityCard(String identityCard) {
        return restTemplate.getForObject(AppConfig.backendUrl + "/resident/checkresidentexistbyidentitycard?identityCard={identityCard}", boolean.class, identityCard);
    }

    public Resident findResidentByAccount(Account profile) {
        return restTemplate.postForObject(AppConfig.backendUrl + "/resident/findresidentbyaccount", profile, Resident.class);
    }

    public List<String> findDistinctNonNullHouseId(Account account, Resident resident) {
        AccountResidentWrapper wrapper = new AccountResidentWrapper(account, resident);
        return Arrays.asList(restTemplate.postForObject(AppConfig.backendUrl + "/resident/finddistinctnonnullhouseid", wrapper, String[].class));
    }

    public void updateResident(Resident resident) {
        restTemplate.postForEntity(AppConfig.backendUrl + "/resident/updateresident", resident, Void.class);
    }

    public void save(Resident resident) {
        restTemplate.postForEntity(AppConfig.backendUrl + "/resident/save", resident, Void.class);
    }

    public List<Resident> findResidentsByFilters(String houseNameFilter, String roleFilter, String searchFilter) {
        return Arrays.asList(restTemplate.getForObject(AppConfig.backendUrl + "/resident/findresidentsbyfilters?houseNameFilter={houseNameFilter}&roleFilter={roleFilter}&searchFilter={searchFilter}", Resident[].class, houseNameFilter, roleFilter, searchFilter));
    }

    public void deleteResidentById(Integer id) {
        restTemplate.delete(AppConfig.backendUrl + "/resident/deleteresidentbyid?id={id}", id);
    }

    public List<Resident> residentSearchResidentsByFilters(Integer residentId, String houseNameFilter, String roleFilter, String searchFilter) {
        return Arrays.asList(restTemplate.getForObject(AppConfig.backendUrl + "/resident/residentsearchresidentsbyfilters?residentId={residentId}&houseNameFilter={houseNameFilter}&roleFilter={roleFilter}&searchFilter={searchFilter}", Resident[].class, residentId, houseNameFilter, roleFilter, searchFilter));
    }
}
