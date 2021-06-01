package com.example.hotelmanagement.entities;

import com.example.hotelmanagement.enums.DeviceType;
import com.example.hotelmanagement.enums.Power;
import lombok.Data;

import java.time.Instant;
import java.util.Objects;

@Data
public class Device {

    DeviceType deviceType;
    Instant lastUsed;
    Power power;

    public static Device setUpAc(Power power) {
        return setUpDevice(DeviceType.AC,power);
    }

    public static Device setUpLight(Power power) {
        return setUpDevice(DeviceType.LIGHT,power);
    }

    private static Device setUpDevice(DeviceType deviceType,Power power) {
        Device device =  new Device();
        device.deviceType = deviceType;
        device.power = power;
        device.lastUsed = Instant.now();
        return device;
    }


    public void powerOn(){
        this.power = Power.ON;
        lastUsed = Instant.now();
    }

    public void powerOff(){
        this.power = Power.OFF;
        lastUsed = Instant.now();
    }


    public boolean isDeviceOn(){
       return  Power.ON.equals(this.getPower());
    }

}
