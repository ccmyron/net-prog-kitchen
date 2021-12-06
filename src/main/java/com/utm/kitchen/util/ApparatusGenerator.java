package com.utm.kitchen.util;

import com.utm.kitchen.apparatus.Apparatus;
import com.utm.kitchen.apparatus.Oven;
import com.utm.kitchen.apparatus.Stove;

import java.util.LinkedList;
import java.util.List;

public class ApparatusGenerator {
    public static List<Apparatus> generateApparatus(int nrOfStoves, int nrOfOvens) {
        List<Apparatus> generatedApparatus = new LinkedList<>();

        for (int i = 0; i < nrOfOvens; i++) {
            generatedApparatus.add(new Oven());
        }

        for (int i = 0; i < nrOfStoves; i++) {
            generatedApparatus.add(new Stove());
        }

        return generatedApparatus;
    }
}
