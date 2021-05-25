package com.example.hotelmanagement.util;


import com.example.hotelmanagement.enums.DeviceType;

public class HMUtil {

    private HMUtil(){}

    public static String formAcId(int floorId, int corridorId){
            return floorId+DeviceType.AC.name()+corridorId;
    }

    public static String formLightId(int floorId, int corridorId){
        return floorId+DeviceType.LIGHT.name()+corridorId;
    }

    public static int getFloorIdFromLightId(String lightId){
        return Integer.parseInt(lightId.split(DeviceType.LIGHT.name())[0]);
    }
}
