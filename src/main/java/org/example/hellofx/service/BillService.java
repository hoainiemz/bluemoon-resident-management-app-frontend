package org.example.hellofx.service;

import org.example.hellofx.AppConfig;
import org.example.hellofx.model.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
//@Transactional
public class BillService {
    @Autowired
    RestTemplate restTemplate;

    public Bill save(Bill bill) {
        return restTemplate.postForObject(AppConfig.backendUrl + "/bill/save", bill, Bill.class);
    }

    public Bill findBillByBillId(Integer billId) {
        return restTemplate.getForObject(AppConfig.backendUrl + "/bill/findbillbybillid?billId={billId}", Bill.class, billId);
    }
    
//    @Transactional(readOnly = true)
    public List<Bill> findBillsByFilters(int requireFilter, int dueFilter, String searchFilter) {
        ResponseEntity<List<Bill>> response = restTemplate.exchange(
                AppConfig.backendUrl + "/bill/findbillbyfilters?requireFilter={requireFilter}&dueFilter={dueFilter}&searchFilter={searchFilter}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Bill>>() {},
                requireFilter,
                dueFilter,
                searchFilter
        );
        return response.getBody();
    }

//    @Transactional
    public void updateBill(Bill bill) {
        restTemplate.postForEntity(AppConfig.backendUrl + "/bill/updatebill", bill, Void.class);
    }

    public void deleteBillById(Integer id) {
        restTemplate.delete(AppConfig.backendUrl + "/bill/deletebillbyid?id={id}", id);
    }
}
