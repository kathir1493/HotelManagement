package com.example.hotelmanagement.entities;

import com.example.hotelmanagement.enums.Power;
import lombok.Data;

import java.time.Instant;

@Data
public class Device {

    DeviceType deviceType;
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
        return device;
    }


    public void powerOn(){
        this.power = Power.ON;
    }

    public void powerOff(){
        this.power = Power.OFF;
    }


    public boolean isDeviceOn(){
       return  Power.ON.equals(this.getPower());
    }


    public static int getAcUnit(){
        return DeviceType.AC.getUnit();
    }

    public static int getLightUnit(){
        return DeviceType.LIGHT.getUnit();
    }


    enum  DeviceType {
        AC(10),LIGHT(5);

        int unit;

        DeviceType(int unit) {
            this.unit = unit;
        }

        public int getUnit(){
            return this.unit;
        }

    }



}
