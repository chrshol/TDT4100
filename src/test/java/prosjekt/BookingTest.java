package prosjekt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BookingTest {

    private Booking booking;
    private Room room;
    private Room room2;
    private Guest guest;
    private Guest invalidGuest;
    private Guest invalidGuest2;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private LocalDate checkIn2;
    private LocalDate checkOut2;
    private Map<String, ArrayList<ArrayList<LocalDate>>> bookedDates; 
    ArrayList<LocalDate> bookedDates1;
    ArrayList<LocalDate> bookedDates2;
    


    @BeforeEach
    public void setUp(){
        checkIn = LocalDate.parse("2022-06-12");
        checkOut = LocalDate.parse("2022-06-15");
        checkIn2 = LocalDate.parse("2022-06-20");
        checkOut2 = LocalDate.parse("2022-07-25");
        bookedDates1 = new ArrayList<LocalDate>(Arrays.asList(checkIn, checkOut));
        bookedDates2 = new ArrayList<LocalDate>(Arrays.asList(checkIn2, checkOut2));
        guest = new Guest("Hei Hopp", "HeiHopp@gmail.com", 3, checkIn, checkOut);
        invalidGuest = new Guest("Heidi Hei", "Heidi@gmail.com", 3, checkIn.minusDays(3), checkIn.plusDays(1));
        invalidGuest2 = new Guest("Heidi Hei", "Heidi@gmail.com", 3, checkIn.plusDays(1), checkOut.plusDays(2));
        room = new Room("Twin Room", 2, 1400.0);
        room2 = new Room("Double Room", 2, 1600.0);
        booking = new Booking();
        bookedDates = new HashMap<String,ArrayList<ArrayList<LocalDate>>>();
        bookedDates.put("Twin Room", new ArrayList<ArrayList<LocalDate>>(Arrays.asList(bookedDates1)));
        bookedDates.put("Double Room", new ArrayList<ArrayList<LocalDate>>(Arrays.asList(bookedDates2)));
        bookedDates.put("Family Room", new ArrayList<ArrayList<LocalDate>>(Arrays.asList(bookedDates1 ,bookedDates2)));
        
    }

    @Test
    @DisplayName("Tester liste med gjesteinformasjon")
    public void testGuestInfo(){
        booking.setGuestInfo(guest);
        assertEquals(List.of("Hei Hopp", "HeiHopp@gmail.com", "3", "2022-06-12", "2022-06-15")
        , booking.getGuestInfo());


    }


    @Test
    @DisplayName("Tester om bookingen er gyldig")
    public void testBookRoom(){
        assertThrows(IllegalArgumentException.class , () -> {
            booking.bookRoom(room, guest);
        }, "Utløser unntak når det er flere gjester enn det er plass til på rommet");

        bookedDates.put("Suit", new ArrayList<ArrayList<LocalDate>>());
        bookedDates.get("Suit").add(bookedDates1);
        assertEquals(List.of(bookedDates1), bookedDates.get("Suit"));
        assertEquals(4, bookedDates.size());

        bookedDates.get("Twin Room").add(bookedDates2);
        assertEquals(List.of(bookedDates1, bookedDates2), bookedDates.get("Twin Room"));

        
        assertThrows(IllegalArgumentException.class, () -> {
            booking.bookRoom(room, guest);
        }, "utløser unntak hvis man prøver å sjekke inn på samme dag som en annen booking har innsjekk på");

        assertThrows(IllegalArgumentException.class, () -> {
            booking.bookRoom(room, invalidGuest);
        }, "utløser unntak hvis man prøver å sjekke inn på et rom som ikke er ledig for de oppgitte datoene");
        
        assertThrows(IllegalArgumentException.class, () -> {
            booking.bookRoom(room, invalidGuest2);
        }, "utløser unntak hvis man prøver å sjekke inn på et rom som ikke er ledig for de oppgitte datoene");
        
        

    }


    @Test
    @DisplayName("Tester kansellering av reservasjon")
    public void testCancelReservation(){

        assertEquals(true, bookedDates.get("Family Room").remove(bookedDates2), "tester at value fjernes fra key");
    }

    @Test
    @DisplayName("Tester om bookede datoer blir lagt til i HashMap (brukes kun for lesing fra fil)")
    public void testAddBookedDatesFromFile(){
        bookedDates.put("Queen Suit", new ArrayList<ArrayList<LocalDate>>());
        bookedDates.get("Queen Suit").add(bookedDates1);
        assertEquals(List.of(bookedDates1), bookedDates.get("Queen Suit"));
        assertEquals(4, bookedDates.size());

        bookedDates.get("Twin Room").add(bookedDates2);
        assertEquals(List.of(bookedDates1, bookedDates2), bookedDates.get("Twin Room"));

    }

    @Test
    @DisplayName("Tester antall netter mellom to datoer")
    public void testGetNights(){
        
        assertEquals(5, booking.getNights(LocalDate.now(), LocalDate.now().plusDays(5)));
        assertEquals(1, booking.getNights(LocalDate.of(2022, 07, 11), LocalDate.of(2022, 07, 12)));
    }

    @Test
    @DisplayName("Tester total pris for oppholdet")
    public void testTotalPrice(){
        booking.setTotalPrice(room, checkIn, checkOut, 2);
        assertEquals(3570.0, booking.getTotalPrice());
        booking.setTotalPrice(room2, LocalDate.now(), LocalDate.now().plusDays(1), 0);
        assertEquals(1600.0, booking.getTotalPrice());


    }
    
}
