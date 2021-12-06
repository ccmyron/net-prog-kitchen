package com.utm.kitchen.controllers;

import com.utm.kitchen.model.Order;
import com.utm.kitchen.service.KitchenService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

@RestController
public class KitchenController {
    Logger log = LogManager.getLogger(KitchenController.class);

    @PostMapping(value="/order", consumes="application/json", produces="application/json")
    public String getOrder(@RequestBody Order order) {
        log.info("{} received", order);
        order.init();
        KitchenService.getInstance().addOrder(order);
        return "Order received successfully and placed in queue!";
    }
}
