package com.utm.kitchen.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Order {
    private int id;
    private List<Integer> items;
    private int priority;
    private int maxWait;
}
