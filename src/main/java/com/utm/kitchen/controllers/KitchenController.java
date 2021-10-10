package com.utm.kitchen.controllers;

import com.utm.kitchen.model.Order;
import com.utm.kitchen.service.KitchenService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

@RestController
public class KitchenController {

    @PostMapping(value="/receive-order", consumes="application/json", produces="application/json")
    public String getOrder(@RequestBody Order order) {

        Logger log = LogManager.getLogger(KitchenController.class);

        log.info(order);
        KitchenService.getInstance().addOrderToList(order);

        return "Order received successfully and placed in queue!";
    }
}
