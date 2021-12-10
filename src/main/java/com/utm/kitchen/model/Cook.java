package com.utm.kitchen.model;

import com.utm.kitchen.apparatus.Apparatus;
import com.utm.kitchen.apparatus.ApparatusType;
import com.utm.kitchen.service.KitchenService;
import com.utm.kitchen.util.Properties;
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
import java.util.List;
import java.util.concurrent.*;

@Data
public class Cook implements Runnable {

    Logger log = LogManager.getLogger(Cook.class);

    private int rank;
    private int proficiency;
    private String name;
    private String catchPhrase;
    private boolean isCooking;
    private Order orderTaken;
    private Semaphore semaphore;

    public Cook(int rank, int proficiency, String name, String catchPhrase, boolean isCooking) {
        this.rank = rank;
        this.proficiency = proficiency;
        this.name = name;
        this.catchPhrase = catchPhrase;
        this.isCooking = isCooking;
        this.semaphore = new Semaphore(1);
    }

    public void returnOrder(Order order) {
        this.isCooking = false;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:9090/distribution";

        try {
            URI uri = new URI(url);
            HttpEntity<Order> requestEntity = new HttpEntity<>(order, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(uri, requestEntity, String.class);
            log.info(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.orderTaken = null;
    }

    @SneakyThrows
    private void cook(Food food, CountDownLatch latch) {
        Apparatus apparatus = null;

        synchronized (KitchenService.getInstance()) {
            if (food.getCookingApparatus() != null && food.getCookingApparatus().equals("Stove")) {
                synchronized (KitchenService.getInstance().getAvailableApparatuses()) {
                    apparatus = KitchenService.getInstance().findFreeApparatus(ApparatusType.STOVE);
                    apparatus.use();
                }
            } else if (food.getCookingApparatus() != null && food.getCookingApparatus().equals("Oven")) {
                synchronized (KitchenService.getInstance().getAvailableApparatuses()) {
                    apparatus = KitchenService.getInstance().findFreeApparatus(ApparatusType.OVEN);
                    apparatus.use();
                }
            }
        }

        TimeUnit.MILLISECONDS.sleep((long) food.getPrepTime() * Properties.TIME_UNIT);

        if (apparatus != null) {
            apparatus.free();
        }

        latch.countDown();
    }

    @SneakyThrows
    @Override
    public void run() {
        Order foundOrder;

        PriorityBlockingQueue<Order> orders = KitchenService.getInstance().getOrderList();

        while (true) {
            Order order = orders.take();
            log.info("{} is preparing order with id {}", this.name, order.getId());
            semaphore.acquire();
            Food food = order.fetchUnpreparedFood();
            semaphore.release();

            ExecutorService executorService = Executors.newFixedThreadPool(proficiency);

            CountDownLatch latch = new CountDownLatch(proficiency);

            while (food != null) {
                Food finalFood = food;
                executorService.execute(() -> cook(finalFood, latch));
                semaphore.acquire();
                order.setFoodPrepared(food);
                semaphore.release();
                food = order.fetchUnpreparedFood();
            }

            latch.await();

            foundOrder = order;

            KitchenService.getInstance().setFinishedOrders(KitchenService.getInstance().getFinishedOrders() + 1);

            Order finalFoundOrder = foundOrder;
            returnOrder(finalFoundOrder);

            KitchenService.getInstance().removeOrder(foundOrder);

//            log.info(catchPhrase);
//            System.out.println("Current = " + KitchenService.getInstance().getCurrentOrders());
//            System.out.println("Finished = " + KitchenService.getInstance().getFinishedOrders());
        }
    }
}
