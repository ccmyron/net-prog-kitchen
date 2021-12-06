package com.utm.kitchen.service;

import com.utm.kitchen.apparatus.Apparatus;
import com.utm.kitchen.apparatus.ApparatusType;
import com.utm.kitchen.apparatus.Oven;
import com.utm.kitchen.apparatus.Stove;
import com.utm.kitchen.model.Cook;
import com.utm.kitchen.model.Food;
import com.utm.kitchen.model.Menu;
import com.utm.kitchen.model.Order;
import com.utm.kitchen.util.ApparatusGenerator;
import com.utm.kitchen.util.CookGenerator;
import com.utm.kitchen.util.OrderComparator;
import com.utm.kitchen.util.Properties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;

@Getter
public class KitchenService {

    private static KitchenService singleInstance;

    private boolean isKitchenRunning;

    private final List<Food> menu = Menu.fillMenu();
    @Setter private List<Cook> cooks = CookGenerator.generateCooks();
    private volatile PriorityBlockingQueue<Order> orderList =
            new PriorityBlockingQueue<>(20, new OrderComparator());
    private final List<Apparatus> availableApparatuses =
            ApparatusGenerator.generateApparatus(Properties.STOVES, Properties.OVENS);

    /* Private constructor to initialize the instance as a singleton */
    private KitchenService() {}

    public static KitchenService getInstance() {
        if (singleInstance == null) {
            singleInstance = new KitchenService();
        }

        return singleInstance;
    }

    public Food findItemById(int id) {
        return menu.stream()
                .filter(food -> food.getId() == id)
                .collect(Collectors.toList())
                .get(0);
    }

    public synchronized void addOrder(Order order) {
        orderList.add(order);
    }

    public synchronized void removeOrder(Order order) {
        orderList.remove(order);
    }

    public Apparatus findFreeApparatus(ApparatusType apparatus) {
        Apparatus foundApparatus = null;
        synchronized (availableApparatuses) {
            switch (apparatus) {
                case OVEN:
                    while (foundApparatus == null) {
                        for (Apparatus apparatusIterator : availableApparatuses) {
                            if (apparatusIterator.isAvailable() && apparatusIterator instanceof Oven) {
                                foundApparatus = apparatusIterator;
                            }
                        }
                    }
                    break;
                case STOVE:
                    while (foundApparatus == null) {
                        for (Apparatus apparatusIterator : availableApparatuses) {
                            if (apparatusIterator.isAvailable() && apparatusIterator instanceof Stove) {
                                foundApparatus = apparatusIterator;
                            }
                        }
                    }
                    break;
            }
        }

        return foundApparatus;
    }

    public void openKitchen() {
        isKitchenRunning = true;
        for (Cook cook : cooks) {
            Thread thread = new Thread(cook);
            thread.start();
        }
    }
}
