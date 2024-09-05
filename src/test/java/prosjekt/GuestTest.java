package prosjekt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GuestTest {

    
    private Guest guest;
    private String fullName;
    private String email;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int totalGuests;
    
    @BeforeEach
    public void setup() {

        guest = new Guest(fullName, email, totalGuests, checkIn, checkOut);
    }
    


    @Test
    @DisplayName("Tester gyldig email")
    public void testSetEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            guest.setEmail("test123.gmail.no");
        }, "utløser unntak hvis email ikke har gyldig landekode etter punktum, eller består av en @");
        //Må også teste om gyldig landekode med finnes helt likt i personTest for øving 2
        guest.setEmail("test123@gmail.com");
        assertEquals("test123@gmail.com", guest.getEmail());
    }

    @Test
    @DisplayName("Tester gyldig fullt navn")
    public void testValidFullName(){
        assertThrows(IllegalArgumentException.class, () -> {
            guest.setFirstName("");   
        }, "utløser unntak hvis first name er blank");
        assertThrows(IllegalArgumentException.class, () -> {
            guest.setLastName("");
        }, "utløser unntak hvis last name er blank");
        guest.setFullName("Hei", "Hopp");
        assertEquals("Hei Hopp", guest.getFullName());

    }

    @Test
    @DisplayName("Tester gyldig dato")
    public void testValidDates(){
        assertThrows(IllegalArgumentException.class, () -> {
            guest.setCheckInDate(LocalDate.now().minusDays(3));
        }, "utløser unntak hvis datoen prøves å sette til en dato som allerede har vært");
        
        guest.setCheckInDate(LocalDate.now().plusDays(2));

        assertThrows(IllegalArgumentException.class, () -> {
            guest.setCheckOutDate(LocalDate.now().plusDays(1));
        }, "utløser unntak hvis check out er før check in");

        assertThrows(IllegalArgumentException.class, () -> {
            guest.setCheckOutDate(LocalDate.now().minusDays(2));
        }, "utløser unntak hvis check out er før dagens dato");

    }

    @Test
    @DisplayName ("Tester liste med booked datoer")
    public void testListOfBookedDates(){
        LocalDate checkIn = LocalDate.parse("2022-06-12");
        LocalDate checkOut = LocalDate.parse("2022-06-15");
        guest.setListOfBookedDates(checkIn,checkOut);
        assertEquals(List.of(checkIn, checkOut), guest.getListOfBookedDates());
        
    }


    



}
