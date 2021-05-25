package com.example.hotelmanagement.enums;

public enum  DeviceType {
    AC(10),LIGHT(5);

    int unit;

    DeviceType(int unit) {
        this.unit = unit;
    }

    public int getUnit(){
        return this.unit;
    }

}
