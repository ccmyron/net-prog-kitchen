package com.utm.kitchen.model;

import com.utm.kitchen.service.KitchenService;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Data
public class Cook implements Runnable {

    Logger log = LogManager.getLogger(Cook.class);

    private int rank;
    private int proficiency;
    private String name;
    private String catchPhrase;
    private boolean isCooking;
    private Order orderTaken;

    public Cook(int rank, int proficiency, String name, String catchPhrase, boolean isCooking) {
        this.rank = rank;
        this.proficiency = proficiency;
        this.name = name;
        this.catchPhrase = catchPhrase;
        this.isCooking = isCooking;
    }

    private void prepareFood() {
        this.isCooking = true;
        orderTaken = KitchenService.getInstance().getOrderList().remove();
    }

    public void returnOrder(Order order) {
        this.isCooking = false;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:9090/receive-order";

        try {
            URI uri = new URI(url);
            HttpEntity<Order> requestEntity = new HttpEntity<>(order, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(uri, requestEntity, String.class);
            log.info(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.orderTaken = null;
        log.info(this.catchPhrase);
    }

    @SneakyThrows
    @Override
    public void run() {
        while (KitchenService.getInstance().isKitchenRunning()) {
            if (KitchenService.getInstance().getOrderList().isEmpty()) {
                log.info("{} is waiting for orders", this.name);
                TimeUnit.SECONDS.sleep(5);
            } else {
                prepareFood();
                TimeUnit.SECONDS.sleep(new Random().nextInt(20));
                returnOrder(this.orderTaken);
            }
        }
    }
}
