package com.utm.kitchen.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Order {
    private UUID id;
    private List<Integer> items;
    private int priority;
    private int maxWait;
}
