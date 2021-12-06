package com.utm.kitchen.model;

import com.utm.kitchen.service.KitchenService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Order {
    private UUID id;
    private List<Integer> items;
    private boolean[] itemsDone;
    private boolean[] itemsUsed;
    private int priority;
    private int maxWait;
    private boolean isReady;

    public Order() {}

    public void init() {
        itemsDone = new boolean[items.size()];
        itemsUsed = new boolean[items.size()];

        for (Boolean item : itemsDone) {
            item = false;
        }

        for (Boolean item : itemsUsed) {
            item = false;
        }
    }

    public List<Food> getFoodsByItemId() {
        List<Food> foods = KitchenService.getInstance().getMenu();
        List<Food> returnedFoods = new LinkedList<>();

        foods.forEach(food -> {
            if (items.contains(food.getId())) {
                returnedFoods.add(food);
            }
        });

        return returnedFoods;
    }

    @SneakyThrows
    public void setFoodPrepared(Food item) {
        for (int itm : items) {
            if (itm == item.getId()) {
                itemsDone[items.indexOf(itm)] = true;
            }
        }
    }

    @SneakyThrows
    public Food fetchUnpreparedFood() {
        for (int item : items) {
            int indx = items.indexOf(item);
            if (!itemsDone[indx] && !itemsUsed[indx]) {
                itemsUsed[indx] = true;
                return KitchenService.getInstance().findItemById(item);
            }
        }
        return null;
    }
}


