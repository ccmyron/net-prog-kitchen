package com.utm.kitchen.model;

import com.utm.kitchen.apparatus.Apparatus;
import com.utm.kitchen.apparatus.ApparatusType;
import com.utm.kitchen.service.KitchenService;
import com.utm.kitchen.util.Properties;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    @SneakyThrows
    private void cook(Order order, Food food) {
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

        log.info("preparing {}", food);
        TimeUnit.MILLISECONDS.sleep((long) food.getPrepTime() * Properties.TIME_UNIT);
        order.setFoodPrepared(food);
        log.info("done {}", food);

        if (apparatus != null) {
            apparatus.free();
        }
    }

    @SneakyThrows
    @Override
    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(this.proficiency);
        List<Order> activeOrders;
        List<Food> doableFood;

        while (true) {
            activeOrders = KitchenService.getInstance().getHeadCook().getActiveOrders();
            synchronized (activeOrders) {
                for (Order order : activeOrders) {
                    semaphore.acquire();
                    doableFood = order.getDoableFoods(this.rank);
                    semaphore.release();
                    if (!doableFood.isEmpty()) {
                        for (Food food : doableFood) {
                            executorService.execute(() -> cook(order, food));
                        }
                    }
                }
            }
        }
    }
}
