package com.example.hotelmanagement.entities;

import com.example.hotelmanagement.util.HMUtil;

import java.time.Instant;
import java.util.*;

public class Hotel {

    private List<Floor> floors;

    private int noOfFloors;

    private int maxInactiveSeconds;

    private Queue<Device> activeLights ;


    public static Hotel setUpHotel(int noOfFloors,int noOfMainCorridors, int noOfSubCorridors,int maxInactiveSeconds){

        Hotel hotel =  new Hotel();

        hotel.noOfFloors = noOfFloors;

        hotel.floors = new ArrayList<>();

        hotel.activeLights = new PriorityQueue<>();

        hotel.maxInactiveSeconds = maxInactiveSeconds;

        for (int i = 1 ; i <= noOfFloors ; i++){
            Floor floor =  Floor.setUpNewFloor(i,noOfMainCorridors,noOfSubCorridors);
            hotel.floors.add(floor);
        }

        hotel.getStatus();

        return hotel;

    }

    public void onMovement(int floorId,int subCorridorId){

        if(floorId > noOfFloors || floorId < 1){
            System.out.println("\n\nInvalid Floor Number ");
            return;
        }

        System.out.println("\n\nOn Movement : Floor : "+ floorId + " Sub corridor Id :" +subCorridorId);


        Floor floor = floors.get(floorId-1);
        Device light = floor.turnOnCorridor(subCorridorId);

        if(light!=null) {
            activeLights.add(light);
            getStatus();
        }
    }

    public void onNoMovement(){

        boolean onChange = false;

        while (!activeLights.isEmpty()){
           Device light = activeLights.peek();
           if(isInActive(light)){
               int floorId = HMUtil.getFloorIdFromLightId(light.getDeviceId());
               floors.get(floorId-1).restoreToDefault(light.getDeviceId());
               activeLights.poll();
               onChange =  true;
           }else {
               break;
           }
        }

        if(onChange) {
            System.out.println("\n\nOn No movement :");
            getStatus();
        }
    }


    public void getStatus(){
        for(Floor floor : this.floors){
            floor.printFloorStatus();
        }
    }

    private boolean isInActive(Device device){
        return  Instant.now().minusSeconds(maxInactiveSeconds).isAfter(device.getLastUsed());
    }
}
