package com.example.hotelmanagement.entities;

import com.example.hotelmanagement.enums.DeviceType;
import com.example.hotelmanagement.util.CorridorComparatorOnLastMovement;

import java.util.*;

public class Floor {

    private int currentPowerConsumption;
    private int maxPowerConsumption;

    private List<Corridor> mainCorridors;

    private List<Corridor> subCorridors;

    // To turn of the AC of a SubCorridor which was Not used for a Longer time to Optimise Power
    private Deque<Corridor> subCorridorsWithAcOn ;


    // To Turn off the lights on No movement
    private Queue<Corridor> subCorridorsWithLightsOn ;


   /*  Mapping of SubCorridor AC which was turned off During turning ON of the light in a SubCorridor
        On no movement AC from this corridor will be Turned On */
    private HashMap<Corridor,Corridor> corridorMapping ;


    public static Floor setUpNewFloor(int noOfMainCorridors, int noOfSubCorridors){

        Floor floor =  new Floor();

        floor.maxPowerConsumption =   noOfMainCorridors * (DeviceType.AC.getUnit() + DeviceType.LIGHT.getUnit()) + ( noOfSubCorridors * DeviceType.AC.getUnit() );
        floor.currentPowerConsumption =   floor.maxPowerConsumption;

        floor.mainCorridors = setUpMainCorridors(noOfMainCorridors);
        floor.subCorridors = setUpSubCorridors(noOfSubCorridors);

        floor.subCorridorsWithAcOn = new LinkedList<>(floor.subCorridors);
        floor.subCorridorsWithLightsOn = new PriorityQueue<>(new CorridorComparatorOnLastMovement());

        floor.corridorMapping =  new HashMap<>();

        return floor;
    }


    private static List<Corridor> setUpMainCorridors(int numberOfCorridors) {

        List<Corridor> corridors = new ArrayList<>();

        for(int corridorNo = 0 ; corridorNo < numberOfCorridors ; corridorNo++){
            corridors.add(Corridor.setupMainCorridor());
        }

        return corridors;
    }

    private static List<Corridor> setUpSubCorridors(int numberOfCorridors) {

        List<Corridor> corridors = new ArrayList<>();

        for(int corridorNo = 0 ; corridorNo < numberOfCorridors ; corridorNo++){
            corridors.add(Corridor.setupSubCorridor());
        }

        return corridors;
    }


    /* Turns Light ON of a Corridor On Movement and Turns of Other corridors AC
        which was Not used for the longest time on the same floor */

    public Corridor turnOnCorridor(int corridorId){

        if(corridorId > subCorridors.size() || corridorId < 0){
            System.out.println("\n\nInvalid CorridorNumber Number ");
            return null;
        }

       Corridor corridor =  this.subCorridors.get(corridorId);

        if(checkIfPowerReduceRequired(corridor)) {
            turnOffOtherCorridorAc(corridor);
        }

        turnLightOn(corridor);
        turnAcOn(corridor);

        return corridor;
    }



    public boolean checkIfPowerReduceRequired(Corridor corridor) {

        int totalConsumption = currentPowerConsumption;

        if(!corridor.isLightOn()){
            totalConsumption += DeviceType.LIGHT.getUnit();
        }

        if(!corridor.isAcOn()){
            totalConsumption += DeviceType.AC.getUnit();
        }

        return  totalConsumption > maxPowerConsumption ;

    }

    public void onNoMovement(int maxInactiveSeconds){

        boolean onChange = false;

        while (!subCorridorsWithLightsOn.isEmpty()){
            Corridor corridor = subCorridorsWithLightsOn.peek();

            if(corridor.isNoMovement(maxInactiveSeconds)){
                restoreToDefault(corridor);
                onChange =  true;
            }else {
                break;
            }
        }

        if(onChange) {
            System.out.println("\n\nOn No movement :");
            printFloorStatus();
        }
    }


    public void printFloorStatus() {

        System.out.println("Max Power Per floor : "+maxPowerConsumption+" Current PowerUsage : "+ currentPowerConsumption);

        for(Corridor mainCorridor : mainCorridors){
            System.out.println("Main corridor  Light " + mainCorridor.isLightOn() + ": ON AC: " + mainCorridor.isAcOn());
        }

        int i = 0;

        for(Corridor subCorridor : subCorridors){
            System.out.println("Sub corridor  Light " + i++ + ": " + subCorridor.isLightOn() + " AC:" + subCorridor.isAcOn());
        }

    }

    private void turnLightOn(Corridor corridor){

        if(!corridor.isLightOn()) {
            currentPowerConsumption+=DeviceType.LIGHT.getUnit();
            corridor.turnLightOn();

        }

        // To Update the last used time

        subCorridorsWithLightsOn.remove(corridor);

        corridor.updateLastMovement();

        subCorridorsWithLightsOn.add(corridor);
    }


    private void turnLightOff(Corridor corridor){

        if(corridor.isLightOn()) {
            currentPowerConsumption-=DeviceType.LIGHT.getUnit();
        }

        corridor.turnLightOff();
        subCorridorsWithLightsOn.remove(corridor);
    }


    private void turnAcOn(Corridor corridor){

        if(!corridor.isAcOn()) {
            currentPowerConsumption+=DeviceType.AC.getUnit();
        }

        corridor.turnAcOn();

        // To Update the last used

        subCorridorsWithAcOn.remove(corridor);
        subCorridorsWithAcOn.add(corridor);

    }

    private void turnAcOff(Corridor corridor){

        if(corridor.isAcOn()) {
            currentPowerConsumption-=DeviceType.AC.getUnit();
        }
        corridor.turnAcOff();
        subCorridorsWithAcOn.remove(corridor);

    }

    /* Method to check AC which was Not used for long time
        and also not to turn of AC of same corridor */

    private Corridor turnOffOtherCorridorAc(Corridor corridor) {

        if(!subCorridorsWithAcOn.isEmpty()) {
            Corridor otherCorridor = subCorridorsWithAcOn.poll();
            if (otherCorridor == corridor ) {
                otherCorridor = subCorridorsWithAcOn.poll();
            }

            if(otherCorridor!=null) {// In case of No active ac other than the current corridor
                turnAcOff(otherCorridor);
                corridorMapping.put(corridor,otherCorridor) ;
                return otherCorridor;
            }
        }

        return null;
    }


    /* To restore to default state on No Movement */

    private void restoreToDefault(Corridor corridor){

        turnLightOff(corridor);

        if(corridorMapping.containsKey(corridor)){
            turnAcOn(corridorMapping.get(corridor));
            corridorMapping.remove(corridor);
        }

    }


    public Corridor getSubCorridor(int id) {
        if(id>0 && id <= subCorridors.size())
            return subCorridors.get(id-1);
        else return null;
    }

}
