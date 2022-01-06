package com.utm.kitchen.model;

import com.utm.kitchen.service.KitchenService;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.PriorityBlockingQueue;

@Data
public class HeadCook implements Runnable {
    PriorityBlockingQueue<Order> orderList;
    final CopyOnWriteArrayList<Order> activeOrders = new CopyOnWriteArrayList<>();

    public void returnOrder(Order order) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:9090/distribution";

        try {
            URI uri = new URI(url);
            HttpEntity<Order> requestEntity = new HttpEntity<>(order, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(uri, requestEntity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    @Override
    public void run() {
        orderList = KitchenService.getInstance().getOrderList();

        while (true) {
            synchronized (activeOrders) {
                if (orderList.peek() != null) {
                    activeOrders.add(orderList.take());
                }
            }

            for (Order order : activeOrders) {
                if (order.allItemsComplete()) {
                    returnOrder(order);
                }
            }
            activeOrders.removeIf(Order::allItemsComplete);
        }
    }
}
