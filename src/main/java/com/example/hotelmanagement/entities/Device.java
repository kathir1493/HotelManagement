package com.example.hotelmanagement.entities;

import com.example.hotelmanagement.enums.DeviceType;
import com.example.hotelmanagement.enums.Power;
import lombok.Data;

import java.time.Instant;
import java.util.Objects;

@Data
public class Device implements Comparable<Device>{

    String deviceId;
    DeviceType deviceType;
    Instant lastUsed;
    Power power;


    public  Device(String deviceId,DeviceType deviceType,Power power) {
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.power = power;
        this.lastUsed = Instant.now();
    }

    public void powerOn(){
        this.power = Power.ON;
        lastUsed = Instant.now();
    }

    public void powerOff(){
        this.power = Power.OFF;
        lastUsed = Instant.now();
    }

    @Override
    public int compareTo(Device o) {
        return this.getLastUsed().compareTo(o.lastUsed);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return Objects.equals(deviceId, device.deviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId);
    }
}
