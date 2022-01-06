package com.utm.kitchen.model;

import com.utm.kitchen.service.KitchenService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.ArrayList;
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

    @SneakyThrows
    public void setFoodPrepared(Food item) {
        for (int itm : items) {
            if (itm == item.getId()) {
                itemsDone[items.indexOf(itm)] = true;
            }
        }
    }

    public List<Food> getDoableFoods(int rank) {
        List<Food> list = new ArrayList<>();

        for (int item : items) {
            Food food = KitchenService.getInstance().findItemById(item);
            int index = items.indexOf(item);
            if (rank >= food.getComplexity() && !itemsDone[index] && !itemsUsed[index]) {
                itemsUsed[index] = true;
                list.add(food);
            }
        }

        return list;
    }

    public boolean allItemsComplete() {
        for (int item : items) {
            int index = items.indexOf(item);
            if (!itemsDone[index]) {
                return false;
            }
        }

        return true;
    }
}


