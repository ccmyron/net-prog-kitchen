package com.utm.kitchen.model;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    public static List<Food> fillMenu() {
        List<Food> menu = new ArrayList<>();

        menu.add(new Food(
                1,
                "Pizza",
                20,
                2,
                "Oven"
        ));
        menu.add(new Food(
                2,
                "Salad",
                10,
                1,
                null
        ));
        menu.add(new Food(
                3,
                "Zeama",
                7,
                1,
                "Stove"
        ));
        menu.add(new Food(
                4,
                "Scallop Sashimi with Meyer Lemon Confit",
                32,
                3,
                null
        ));
        menu.add(new Food(
                5,
                "Island Duck with Mulberry Mustard",
                35,
                3,
                "Oven"
        ));
        menu.add(new Food(
                6,
                "Waffles",
                10,
                1,
                "Stove"
        ));
        menu.add(new Food(
                7,
                "Aubergine",
                20,
                2,
                null
        ));
        menu.add(new Food(
                8,
                "Lasagna",
                30,
                2,
                "Oven"
        ));
        menu.add(new Food(
                9,
                "Burger",
                15,
                1,
                "Oven"
        ));
        menu.add(new Food(
                10,
                "Gyros",
                15,
                1,
                null
        ));

        return menu;
    }
}
