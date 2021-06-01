package com.example.hotelmanagement.entities;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;


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

        Assert.assertTrue(hotel.getFloor(1).getSubCorridor(3).isAcOn());
        Assert.assertTrue(hotel.getFloor(1).getSubCorridor(3).isLightOn());

        Assert.assertTrue(hotel.getFloor(1).getSubCorridor(1).isAcOn());
        Assert.assertTrue(hotel.getFloor(1).getSubCorridor(1).isLightOn());

        Assert.assertFalse(hotel.getFloor(1).getSubCorridor(2).isAcOn());
        Assert.assertFalse(hotel.getFloor(1).getSubCorridor(2).isLightOn());


        Assert.assertTrue(hotel.getFloor(2).getSubCorridor(1).isAcOn());
        Assert.assertTrue(hotel.getFloor(2).getSubCorridor(1).isLightOn());

        Assert.assertTrue(hotel.getFloor(2).getSubCorridor(3).isAcOn());
        Assert.assertTrue(hotel.getFloor(2).getSubCorridor(3).isLightOn());

        Assert.assertFalse(hotel.getFloor(2).getSubCorridor(2).isAcOn());
        Assert.assertFalse(hotel.getFloor(2).getSubCorridor(2).isLightOn());
    }

    @Test
    public void testMovementInAllCorridorsOfFloor(){  // For movement in all corridors of floor at same time
        hotel = Hotel.setUpHotel(2 , 1,3,60);

        hotel.onMovement(1,3);
        hotel.onMovement(1,1);
        hotel.onMovement(1,2);

        // Only Long running ac will be turned off As turning off light wont be a good user experience. Can change according to requirement

        Assert.assertTrue(hotel.getFloor(1).getSubCorridor(3).isLightOn());
        Assert.assertFalse(hotel.getFloor(1).getSubCorridor(3).isAcOn());

        Assert.assertTrue(hotel.getFloor(1).getSubCorridor(1).isLightOn());
        Assert.assertTrue(hotel.getFloor(1).getSubCorridor(1).isAcOn());

        Assert.assertTrue(hotel.getFloor(1).getSubCorridor(2).isLightOn());
        Assert.assertTrue(hotel.getFloor(1).getSubCorridor(2).isAcOn());
    }

    @Test
    public void testWhenOneCorridorWithMovement(){
        hotel = Hotel.setUpHotel(1 , 1,1,60);

        hotel.onMovement(1,1);

        // As it is only one corridor Turning on the ac and Light

        Assert.assertTrue(hotel.getFloor(1).getSubCorridor(1).isLightOn());
        Assert.assertTrue(hotel.getFloor(1).getSubCorridor(1).isAcOn());

    }

    @Test
    public void testNoMovement() throws InterruptedException {
        hotel = Hotel.setUpHotel(1 , 1,3,10); // passing 10

        hotel.onMovement(1,3);
        hotel.onMovement(1,1);
        hotel.onMovement(1,2);

        Thread.sleep(10000); // no movement for 10 seconds

        hotel.onNoMovement();

        Assert.assertFalse(hotel.getFloor(1).getSubCorridor(3).isLightOn());
        Assert.assertTrue(hotel.getFloor(1).getSubCorridor(3).isAcOn());

        Assert.assertFalse(hotel.getFloor(1).getSubCorridor(1).isLightOn());
        Assert.assertTrue(hotel.getFloor(1).getSubCorridor(1).isAcOn());

        Assert.assertFalse(hotel.getFloor(1).getSubCorridor(2).isLightOn());
        Assert.assertTrue(hotel.getFloor(1).getSubCorridor(2).isAcOn());

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

        Assert.assertTrue(hotel.getFloor(1).getSubCorridor(3).isAcOn());
        Assert.assertFalse(hotel.getFloor(1).getSubCorridor(3).isLightOn());

        Assert.assertTrue(hotel.getFloor(1).getSubCorridor(1).isAcOn());
        Assert.assertFalse(hotel.getFloor(1).getSubCorridor(1).isLightOn());

        Assert.assertTrue(hotel.getFloor(1).getSubCorridor(2).isAcOn());
        Assert.assertFalse(hotel.getFloor(1).getSubCorridor(2).isLightOn());


        Assert.assertTrue(hotel.getFloor(2).getSubCorridor(1).isAcOn());
        Assert.assertTrue(hotel.getFloor(2).getSubCorridor(1).isLightOn());

        Assert.assertTrue(hotel.getFloor(2).getSubCorridor(3).isAcOn());
        Assert.assertTrue(hotel.getFloor(2).getSubCorridor(3).isLightOn());

        Assert.assertFalse(hotel.getFloor(2).getSubCorridor(2).isAcOn());
        Assert.assertFalse(hotel.getFloor(2).getSubCorridor(2).isLightOn());

    }


    @Test
    public void testInvalidFloorAndCorridorId() {

        hotel = Hotel.setUpHotel(1 , 1,1,60);

        hotel.onMovement(3,1);  // Invalid Floor Id
        hotel.onMovement(1,4);  // Invalid Corridor Id

        Assert.assertTrue(hotel.getFloor(1).getSubCorridor(1).isAcOn());
        Assert.assertFalse(hotel.getFloor(1).getSubCorridor(1).isLightOn());
    }


}
