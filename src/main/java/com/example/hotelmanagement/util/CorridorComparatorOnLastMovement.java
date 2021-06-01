package com.example.hotelmanagement.util;

import com.example.hotelmanagement.entities.Corridor;
import com.example.hotelmanagement.entities.Device;

import java.util.Comparator;

public class CorridorComparatorOnLastMovement implements Comparator<Corridor> {

    @Override
    public int compare(Corridor o1, Corridor o2) {
        return o2.getLastMovement().compareTo(o1.getLastMovement());
    }
}
