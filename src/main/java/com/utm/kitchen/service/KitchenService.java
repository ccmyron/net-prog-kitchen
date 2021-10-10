package com.utm.kitchen.service;

import com.utm.kitchen.model.Cook;
import com.utm.kitchen.model.Food;
import com.utm.kitchen.model.Menu;
import com.utm.kitchen.model.Order;
import com.utm.kitchen.util.CookGenerator;
import com.utm.kitchen.util.OrderComparator;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.PriorityQueue;

public class KitchenService {

    private static KitchenService singleInstance;

    @Getter private boolean isKitchenRunning;

    @Getter private final List<Food> menu = Menu.fillMenu();
    @Getter @Setter private List<Cook> cooks = CookGenerator.generateCooks();
    @Getter private PriorityQueue<Order> orderList = new PriorityQueue<>(20, new OrderComparator());


    /* Private constructor to initialize the instance as a singleton */
    private KitchenService() {}

    public static KitchenService getInstance() {
        if (singleInstance == null) {
            singleInstance = new KitchenService();
        }

        return singleInstance;
    }

    public void addOrderToList(Order newOrder) {
        orderList.add(newOrder);
    }

    public void openKitchen() {
        isKitchenRunning = true;
        for (Cook cook : cooks) {
            Thread thread = new Thread(cook);
            thread.start();
        }
    }
}
