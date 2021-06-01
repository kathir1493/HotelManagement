package com.example.hotelmanagement.entities;


import java.util.*;

public class Hotel {

    private List<Floor> floors;

    private int noOfFloors;

    private int maxInactiveSeconds;

    public static Hotel setUpHotel(int noOfFloors,int noOfMainCorridors, int noOfSubCorridors,int maxInactiveSeconds){

        Hotel hotel =  new Hotel();

        hotel.noOfFloors = noOfFloors;

        hotel.floors = new ArrayList<>();

        hotel.maxInactiveSeconds = maxInactiveSeconds;

        for (int i = 0 ; i < noOfFloors ; i++){
            Floor floor =  Floor.setUpNewFloor(noOfMainCorridors,noOfSubCorridors);
            hotel.floors.add(floor);
        }

        hotel.getStatus();

        return hotel;

    }

    public void onMovement(int floorId,int subCorridorId){

        if(floorId > noOfFloors || floorId <= 0){
            System.out.println("\n\nInvalid Floor Number ");
            return;
        }

        System.out.println("\n\nOn Movement : Floor : "+ floorId + " Sub corridor Id :" + subCorridorId );

        Floor floor = floors.get(floorId-1);

        floor.turnOnCorridor(subCorridorId-1);
        getStatus();
    }

    public void onNoMovement(){

        for(Floor floor : floors){
            floor.onNoMovement(maxInactiveSeconds);
        }

    }


    public void getStatus(){
        int i=1;
        for(Floor floor : this.floors){
            System.out.println("Floor "+i++);
            floor.printFloorStatus();
        }
    }

    public Floor getFloor(int floorId) {
        if(floorId > 0 && floorId <= floors.size() ){
            return floors.get(floorId-1);
        }

        System.out.println("\n\nInvalid Floor Number ");
        return null;
    }
}
