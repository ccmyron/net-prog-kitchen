package com.utm.kitchen.util;

import com.utm.kitchen.model.Order;

import java.util.Comparator;

public class OrderComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        if (o1.getPriority() > o2.getPriority()) {
            return 1;
        } else if (o2.getPriority() > o1.getPriority()) {
            return -1;
        }

        return 0;
    }


}
