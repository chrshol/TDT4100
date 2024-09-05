package prosjekt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RoomTest {

    Room room;

    @Test
    @DisplayName("Tester toString-metoden")
    public void testToString(){
        Room room = new Room("Test Room", 2, 1200);
        assertEquals("Test Room" + "\n" + "Guest capasity: " + 2 + "\n" + "Price per night: " + 1200.0
        ,room.toString());
        
    }
    
}
