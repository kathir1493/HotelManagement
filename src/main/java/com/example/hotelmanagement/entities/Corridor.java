package com.example.hotelmanagement.entities;


import com.example.hotelmanagement.enums.Power;

import java.time.Instant;

public class Corridor {

    private Device ac;
    private Device light;

    /* Maintains the last movement happened on the Corridor */

    private Instant lastMovement;


    public static Corridor setupMainCorridor(){
        return setupCorridor(CorridorType.MAIN);
    }

    public static Corridor setupSubCorridor(){
        return setupCorridor(CorridorType.SUB);
    }

    private static Corridor setupCorridor( CorridorType type){

        Corridor corridor =  new Corridor();

        corridor.ac = Device.setUpAc(Power.ON);


        if(CorridorType.MAIN.equals(type)) {
            corridor.light = Device.setUpLight(Power.ON);
        } else {
            corridor.light = Device.setUpLight(Power.OFF);
        }

        return corridor;

    }

    public void turnAcOn(){
         ac.powerOn();
    }

    public void turnAcOff(){
        ac.powerOff();
    }

    public void turnLightOn(){
        light.powerOn();
        updateLastMovement();
    }

    public void updateLastMovement(){
        lastMovement = Instant.now();
    }

    public void turnLightOff(){
        light.powerOff();
    }

    public  boolean isLightOn(){
        return light.isDeviceOn();
    }

    public  boolean isAcOn(){
        return ac.isDeviceOn();
    }


    public boolean isNoMovement(int maxInactiveSeconds){
        return  Instant.now().minusSeconds(maxInactiveSeconds).isAfter(lastMovement);
    }

    public Instant getLastMovement() {
        return lastMovement;
    }

    enum CorridorType {
        MAIN,SUB;
    }

}
