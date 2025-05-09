package org.example.hellofx.service;

import org.example.hellofx.AppConfig;
import org.example.hellofx.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
//@Transactional
public class AccountService {
    @Autowired
    private RestTemplate restTemplate;

    public boolean checkAccountExistByUsername(String username) {
        return restTemplate.getForObject(AppConfig.backendUrl + "/account/checkaccountexistbyusername?username={username}", Boolean.class, username);
    }

    public boolean checkAccountExistByEmail(String email) {
        return restTemplate.getForObject(AppConfig.backendUrl + "/account/checkaccountexistbyemail?email={email}", Boolean.class, email);
    }

    public boolean checkAccountExistByPhone(String phone) {
        return restTemplate.getForObject(AppConfig.backendUrl + "/account/checkaccountexistbyphone?phone={phone}", Boolean.class, phone);
    }

    public Account findAccountByUsernameAndPassword(String username, String password) {
        return restTemplate.getForObject(AppConfig.backendUrl + "/account/findaccountbyusernameandpassword?username={username}&password={password}", Account.class, username, password);
    }

    public int passwordChangeQuery(String username, String newPassword) {
        String url = AppConfig.backendUrl + "/account/passwordchangequery?username={username}&newpassword={newpassword}";
    
        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    
        // Create an empty body since we're passing parameters in URL
        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
    
        // Use PUT instead of PATCH
        return restTemplate.exchange(
            url,
            HttpMethod.PUT,
            requestEntity,
            Integer.class,
            username,
            newPassword
        ).getBody();
    }

    public Account findAccountByUserId(int id) {
        return restTemplate.getForObject(AppConfig.backendUrl + "/account/findaccountbyuserid?id={id}", Account.class, id);
    }

    public boolean existsByEmail(String email) {
        return restTemplate.getForObject(AppConfig.backendUrl + "/account/existbyemail?email={email}", Boolean.class, email);
    }

    public boolean existsByPhone(String phone) {
        return restTemplate.getForObject(AppConfig.backendUrl + "/account/existbyphone?phone={phone}", Boolean.class, phone);
    }

    public List<Account> nativeAccountQuery(String query) {
        Account[] accounts = restTemplate.getForObject(AppConfig.backendUrl + "/account/nativeaccountquery?query={query}", Account[].class, query);
        return Arrays.asList(accounts);
    }

    public Optional<Account> findByEmail(String email) {
        Account account = restTemplate.getForObject(AppConfig.backendUrl + "/account/findbyemail?email={email}", Account.class, email);
        return Optional.ofNullable(account);
    }

    public void createAccount(String username, String password, String email, String phone) {
        restTemplate.postForObject(AppConfig.backendUrl + "/account/createaccount?username={username}&password={password}&email={email}&phone={phone}", null, Void.class, username, password, email, phone);
    }

    public void updateAccount(Account account) {
        String url = AppConfig.backendUrl + "/account/updateaccount";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Account> request = new HttpEntity<>(account, headers);

        restTemplate.put(url, request);
    }

    public void save(Account account) {
        String url = AppConfig.backendUrl + "/account/saveaccount";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Account> request = new HttpEntity<>(account, headers);

        restTemplate.postForEntity(url, request, Void.class);
    }

}