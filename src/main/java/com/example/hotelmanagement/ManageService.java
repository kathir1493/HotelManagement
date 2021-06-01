package com.example.hotelmanagement;

import com.example.hotelmanagement.entities.Hotel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.util.Scanner;


/*
 Driver class
*/

@Service
@EnableScheduling
public class ManageService {

    Hotel hotel ;

    @Value("${max.inactive.seconds:60}")
    private int maxInactiveSeconds;


    @EventListener
    public void onApplicationReadyEvent2(ApplicationReadyEvent event) {
        this.initializeHotel();
        this.checkForMovement();
    }


    public void initializeHotel(){

        Scanner scanner =  new Scanner(System.in);

        System.out.println("Number of floors :");

        int floors = scanner.nextInt();

        System.out.println("Main corridors per floor :");

        int mainCorridors = scanner.nextInt();

        System.out.println("Sub corridors per floor :");

        int subCorridors = scanner.nextInt();

        hotel =  Hotel.setUpHotel(floors,mainCorridors,subCorridors,maxInactiveSeconds);

    }


    private void checkForMovement() {
        Scanner scanner =  new Scanner(System.in);

        while (true) {
            int floorNumber = scanner.nextInt();
            int subCorridor =  scanner.nextInt();;
            hotel.onMovement(floorNumber, subCorridor);
        }
    }

    @Scheduled(fixedRateString = "${scheduler.frequency:10000}")
    void onNoMovement(){
        if(hotel!=null)
            hotel.onNoMovement();
    }

}
