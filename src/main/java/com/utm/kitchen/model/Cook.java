package com.utm.kitchen.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Cook {
    private int rank;
    private int proficiency;
    private String name;
    private String catchPhrase;
}
