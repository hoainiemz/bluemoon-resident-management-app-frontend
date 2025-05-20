package org.example.hellofx.service;

import com.github.javafaker.App;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.hellofx.AppConfig;
import org.example.hellofx.dto.PaymentProjectionDTO;
import org.example.hellofx.model.Bill;
import org.example.hellofx.model.InvoiceItem;
import org.example.hellofx.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Service
public class PaymentService {
    @Autowired
    private RestTemplate restTemplate;

    public List<Payment> findPaymentByBillId(Integer billId) {
        return Arrays.asList(restTemplate.getForObject(AppConfig.backendUrl + "/payment/findpaymentbybillid?billId={billId}", Payment[].class, billId));
    }


//    @Transactional
    public void saveAllPayments(List<Payment> payments) {
        restTemplate.postForObject(AppConfig.backendUrl + "/payment/saveallpayments", payments, Void.class);
    }


//    @Transactional
    public void deletePayments(List<Integer> dsOut) {
        restTemplate.delete(AppConfig.backendUrl + "/payment/deletepayments?ids={ids}", dsOut);
    }

    public List<PaymentProjectionDTO> getPaymentProjectionByResidentIdAndFilters(Integer residentId, int stateFilter, int requireFilter, int dueFilter, String searchFilter) {
        ResponseEntity<List<PaymentProjectionDTO>> response = restTemplate.exchange(
                AppConfig.backendUrl + "/payment/getpaymentprojectionbyresidentidandfilters?residentId={residentId}&stateFilter={stateFilter}&requireFilter={requireFilter}&dueFilter={dueFilter}&searchFilter={searchFilter}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PaymentProjectionDTO>>() {},
                residentId,
                stateFilter,
                requireFilter,
                dueFilter,
                searchFilter
        );
        return response.getBody();
    }


//    @Transactional
    public void generatePaymentsForBill(Bill bill) {
        restTemplate.postForObject(AppConfig.backendUrl + "/payment/generatepaymentsforbill", bill, Void.class);
    }

    public byte[] getBillPaymentLink(int paymentId) {
//        return restTemplate.getForObject(AppConfig.backendUrl + "/payment/getbillpaymentlink?paymentId={paymentId}", String.class, paymentId);
        byte[] qrImage = restTemplate.getForObject(
                AppConfig.backendUrl + "/payment/getbillpaymentlink?paymentId={id}",
                byte[].class,
                paymentId
        );
        return qrImage;
    }

    public List<InvoiceItem> getReceipt(Integer paymentId) {
        ResponseEntity<List<InvoiceItem>> response = restTemplate.exchange(
                AppConfig.backendUrl + "/payment/getreceipt?paymentId={paymentId}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<InvoiceItem>>() {},
                paymentId
        );
        return response.getBody();
    }

//    public void export(List<Integer> billIds) throws IOException {
//        UriComponentsBuilder builder = UriComponentsBuilder
//                .fromHttpUrl(AppConfig.backendUrl + "/payment/export")
//                .queryParam("billIds", billIds); // tự format đúng nhiều giá trị
//
//        URI uri = builder.build().toUri();
//
//        ResponseEntity<byte[]> response = restTemplate.exchange(
//                uri,
//                HttpMethod.GET,
//                null,
//                byte[].class
//        );
//
//        if (response.getStatusCode().is2xxSuccessful()) {
//            byte[] fileData = response.getBody();
//            Path path = Paths.get("downloaded-payments.xlsx");
//            Files.write(path, fileData);
//            System.out.println("✅ File Excel đã được tải về tại: " + path.toAbsolutePath());
//        } else {
//            System.err.println("❌ Tải file thất bại: " + response.getStatusCode());
//        }
//    }

    public void export(List<Integer> billIds, Stage ownerWindow) throws Exception {
        // 1. Gọi API lấy file Excel dưới dạng byte[]
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(AppConfig.backendUrl + "/payment/export")
                .queryParam("billIds", billIds);

        URI uri = builder.build().toUri();

        ResponseEntity<byte[]> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                byte[].class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            byte[] fileData = response.getBody();

            // 2. Mở hộp thoại File Explorer để chọn nơi lưu file
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Chọn nơi lưu file Excel");
            fileChooser.setInitialFileName("payments.xlsx");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
            );

            File file = fileChooser.showSaveDialog(ownerWindow); // truyền vào Stage cha

            if (file != null) {
                Files.write(file.toPath(), fileData);
            } else {
                throw new Exception("Người dùng đã hủy lưu file.");
            }
        } else {
            throw new Exception("Tải file thất bại: " + response.getStatusCode());
        }
    }
}
