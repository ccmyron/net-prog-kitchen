package com.utm.kitchen.util;

import com.utm.kitchen.model.Cook;

import java.util.ArrayList;
import java.util.List;

public class CookGenerator {

    public static List<Cook> generateCooks() {

        List<Cook> cooks = new ArrayList<>();

        cooks.add(new Cook(1,
                1,
                "Andras Aurelius",
                "Hello, my name is Andras, but friends call me to biti ebalo",
                false)
        );
        cooks.add(new Cook(2,
                2,
                "Aline Charly",
                "Woman moment",
                false)
        );
        cooks.add(new Cook(3,
                3,
                "Rexanne Shulmanu-Ashared",
                "Salamu alaykum",
                false)
        );
        cooks.add(new Cook(3,
                4,
                "Alex Cornea",
                "ia spati",
                false)
        );

        return cooks;
    }
}
