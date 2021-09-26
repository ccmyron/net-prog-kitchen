package com.utm.kitchen.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Food {
    private int id;
    private String name;
    private int prepTime;
    private int complexity;
    private String cookingApparatus;
}
