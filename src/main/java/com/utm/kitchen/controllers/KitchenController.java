package com.utm.kitchen.controllers;

import com.utm.kitchen.model.Order;
import org.springframework.web.bind.annotation.*;

@RestController
public class KitchenController {

    @PostMapping(value="/order", consumes="application/json", produces="application/json")
    public String sanityCheck(@RequestBody Order order) {

        System.out.println(order);

        return "Order received successfully and placed in queue!";
    }

}
