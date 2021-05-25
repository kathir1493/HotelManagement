package com.example.hotelmanagement.entities;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;


/************  NOTE ****************/

/*

As We are printing the Result not able to add assert
When we are converting this into an api which returns value we shall add Asserts

*/

@RunWith(SpringRunner.class)
public class HotelTest {

    Hotel hotel;

    @Test
    public void testMovement(){
        hotel = Hotel.setUpHotel(2 , 1,3,60);

        hotel.onMovement(1,3);
        hotel.onMovement(1,1);


        hotel.onMovement(2,1);
        hotel.onMovement(2,3);

    }

    @Test
    public void testMovementInAllCorridorsOfFloor(){  // For movement in all corridors of floor at same time
        hotel = Hotel.setUpHotel(2 , 1,3,60);

        hotel.onMovement(1,3);
        hotel.onMovement(1,1);
        hotel.onMovement(1,2);

        // Only Long running ac will be turned off As turning off light wont be a good user experience. Can change according to requirement

    }

    @Test
    public void testWhenOneCorridorWithMovement(){
        hotel = Hotel.setUpHotel(1 , 1,1,60);

        hotel.onMovement(1,1);

        // As it is only one corridor Turning on the ac and Light
    }

    @Test
    public void testNoMovement() throws InterruptedException {
        hotel = Hotel.setUpHotel(1 , 1,3,10); // passing 10

        hotel.onMovement(1,3);
        hotel.onMovement(1,1);
        hotel.onMovement(1,2);

        Thread.sleep(10000); // no movement for 10 seconds

        hotel.onNoMovement();

    }

    @Test
    public void testNoMovementInFloor1() throws InterruptedException {
        hotel = Hotel.setUpHotel(2 , 1,3,10); // passing 10

        hotel.onMovement(1,3);
        hotel.onMovement(1,1);
        hotel.onMovement(1,2);

        Thread.sleep(10000); // no movement for 10 seconds

        hotel.onMovement(2,3);
        hotel.onMovement(2,1);

        hotel.onNoMovement();

    }


    public void testInvalidFloorAndCorridorId() {

        hotel = Hotel.setUpHotel(1 , 1,1,60);

        hotel.onMovement(3,1);  // Invalid Floor Id
        hotel.onMovement(1,4);  // Invalid Corridor Id

    }


}
