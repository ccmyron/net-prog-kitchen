package com.utm.kitchen.controllers;

import com.utm.kitchen.model.Order;
import com.utm.kitchen.service.KitchenService;
import org.springframework.web.bind.annotation.*;

@RestController
public class KitchenController {

    @PostMapping(value="/receive-order", consumes="application/json", produces="application/json")
    public String getOrder(@RequestBody Order order) {

        System.out.println(order);
        KitchenService.getInstance().addOrderToList(order);

        return "Order received successfully and placed in queue!";
    }
}
