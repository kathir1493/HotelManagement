package com.example.hotelmanagement.entities;

import com.example.hotelmanagement.enums.DeviceType;
import com.example.hotelmanagement.enums.Power;
import com.example.hotelmanagement.util.HMUtil;

import java.util.*;

public class Floor {

    private int floorId;
    private int noOfMainCorridors;
    private int noOfSubCorridors;
    private int currentPowerConsumption;
    private int maxcurrentPowerConsumption;

    private HashMap<String,String> lightAcMapping ;

    private Map<String, Device> devices ;

    private Deque<String> activeAc ;

    public static Floor setUpNewFloor(int floorId,int noOfMainCorridors, int noOfSubCorridors){

        Floor floor =  new Floor();

        floor.floorId = floorId;
        floor.noOfMainCorridors = noOfMainCorridors;
        floor.noOfSubCorridors = noOfSubCorridors;

        floor.lightAcMapping =  new HashMap<>();
        floor.devices = new HashMap<>();
        floor.activeAc = new LinkedList<>();

        floor.maxcurrentPowerConsumption =   noOfMainCorridors * (DeviceType.AC.getUnit() + DeviceType.LIGHT.getUnit()) + ( noOfSubCorridors * DeviceType.AC.getUnit() );
        floor.currentPowerConsumption =   floor.maxcurrentPowerConsumption;

        setUpDevicesForSubCorridors(floor);

        return floor;
    }

    private static void setUpDevicesForSubCorridors(Floor floor) {

        for(int corridorId = 1 ; corridorId <= floor.noOfSubCorridors ; corridorId++){

            String acId = HMUtil.formAcId(floor.floorId,corridorId);

            Device ac = new Device(acId,DeviceType.AC, Power.ON);
            floor.devices.put(ac.getDeviceId(),ac);

            floor.activeAc.add(ac.getDeviceId());

            String lightId = HMUtil.formLightId(floor.floorId,corridorId);

            Device light = new Device(lightId,DeviceType.LIGHT,Power.OFF);
            floor.devices.put(light.getDeviceId(),light);
        }
    }


    public Device turnOnCorridor(int corridorId){

        if(corridorId > noOfSubCorridors || corridorId < 1){
            System.out.println("\n\nInvalid CorridorNumber Number ");
            return null;
        }

        String lightID =  HMUtil.formLightId(floorId,corridorId);

        String corridorAcId = HMUtil.formAcId(floorId,corridorId);

        if(checkIfPowerReduceRequired(lightID,corridorAcId))
            turnOffOtherCorridorAc(corridorAcId);

        lightAcMapping.put(lightID, corridorAcId);

        powerOn(lightID);
        powerOn(corridorAcId);

        return devices.get(lightID);
    }

    private boolean checkIfPowerReduceRequired(String lightID, String corridorAcId) {

        int totalConsumption = currentPowerConsumption;

        if(Power.OFF.equals(devices.get(lightID).getPower())){
            totalConsumption += DeviceType.LIGHT.getUnit();
        }

        if(Power.OFF.equals(devices.get(corridorAcId).getPower())){
            totalConsumption += DeviceType.AC.getUnit();
        }


        return  totalConsumption > maxcurrentPowerConsumption ;

    }

    public void restoreToDefault(String lightId) {
        powerOff(lightId);
        powerOn(lightAcMapping.get(lightId));
    }

    public void printFloorStatus() {

        System.out.println("Floor "+floorId);

        System.out.println("Max Power Per floor : "+maxcurrentPowerConsumption+" Current PowerUsage : "+ currentPowerConsumption);

        for(int mainCorridor = 1 ; mainCorridor <= noOfMainCorridors ; mainCorridor++){
            System.out.println("Main corridor  Light " + mainCorridor + ": ON AC: ON ");
        }

        for(int subCorridor = 1 ; subCorridor <= noOfSubCorridors ; subCorridor++){

            String lightId = HMUtil.formLightId(floorId,subCorridor);
            String acId =   HMUtil.formAcId(floorId,subCorridor);

            System.out.println("Sub corridor  Light " + subCorridor + ": " + devices.get(lightId).getPower() + " AC:" + devices.get(acId).getPower());
        }

    }

    private void powerOn(String deviceID){

        Device device = devices.get(deviceID);

        if(Power.OFF.equals(device.getPower())) {
            currentPowerConsumption+=device.getDeviceType().getUnit();
        }
        device.powerOn();

        if(device.getDeviceType().equals(DeviceType.AC))
        {
            activeAc.remove(deviceID);
            activeAc.add(deviceID);
        }

    }

    private void powerOff(String deviceID){

        Device device = devices.get(deviceID);

        if(Power.ON.equals(device.getPower())) {
            currentPowerConsumption-=device.getDeviceType().getUnit();
        }
        device.powerOff();

        if(device.getDeviceType().equals(DeviceType.AC))
        {
            activeAc.remove(deviceID);
        }

    }


    private void turnOffOtherCorridorAc(String corridorAcId) {

        if(!activeAc.isEmpty()) {
            String otherAcId = activeAc.poll();
            if (otherAcId.equals(corridorAcId)) {
                otherAcId = activeAc.poll();
            }

            if(otherAcId!=null) // In case of No active ac other than the current corridor
                powerOff(otherAcId);
        }
    }
}
