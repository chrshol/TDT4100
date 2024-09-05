package prosjekt;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookingsHandlerTest {

    private FileBookingHandler fileBookingHandler = new FileBookingHandler();

    private static final String test_booking_file_content = """
            [Ola Nordmann, test123@gmail.com, 2, 2022-07-07, 2022-07-10];Room;3000.0
            [Kari Nordmann, hei123@gmail.com, 2, 2022-08-12, 2022-08-13];Room2;1100.0
            """.replaceAll("\\R", System.getProperty("line.separator"));

    private static final String invalid_booking_file_content = """
        Ola Nordmann, test123@gmail.com, 3, 2022-07-07, 2022-07-10;Room;4000.0
        Kari Nordmann; hei123@gmail.com; 2; 2022-08-12; 2022-08-13;Room2;2000.0
        """.replaceAll("\\R", System.getProperty("line.separator"));

    

    private Guest guest = new Guest("Ola Nordmann", "test123@gmail.com", 2, LocalDate.parse("2022-07-07"), LocalDate.parse("2022-07-10"));
    private Guest guest2 = new Guest("Kari Nordmann", "hei123@gmail.com", 2, LocalDate.parse("2022-08-12"), LocalDate.parse("2022-08-13"));
    private Room room = new Room("Room", 2, 1000.0);
    private Room room2 = new Room("Room2", 3, 1100.0);
    private String bookedRoomID;
    private double totalPrice;
    private Booking booking1 = new Booking(guest, bookedRoomID, totalPrice);
    private Booking booking2 = new Booking(guest2, bookedRoomID, totalPrice);

    

    private Booking getTestBookingObject() {
        return new Booking();
    }

    private List<Booking> getFilledTestBookingObject() {
        List<Booking> testBookingObject = new ArrayList<Booking>();
        testBookingObject.add(booking1);
        testBookingObject.add(booking2);
        return testBookingObject;
    }
    

    @BeforeAll
    public void setup() throws IOException {
        Files.write(fileBookingHandler.getBookingFile("test_booking").toPath(), test_booking_file_content.getBytes());
        Files.write(fileBookingHandler.getBookingFile("invalid_booking").toPath(), invalid_booking_file_content.getBytes());
    }

    @Test 
    public void testWriteBooking() throws IOException {
        fileBookingHandler.writeBooking("new_bookings", getTestBookingObject());
        File expected = fileBookingHandler.getBookingFile("test_booking");
        File actual = fileBookingHandler.getBookingFile("new_bookings");
        assertNotEquals(expected, actual);
    }

    @Test
    public void testReadBooking() throws IOException {
        List<Booking> expectedBookingObject = getFilledTestBookingObject();
        List<Booking> actualBookingObject = fileBookingHandler.readBooking("test_booking");

        booking1.setBookedRoomID(room);
        booking2.setBookedRoomID(room2);

        booking1.setTotalPrice(room, guest.getCheckIn(), guest.getCheckOut(), 0);
        booking2.setTotalPrice(room2, guest2.getCheckIn(), guest2.getCheckOut(), 0);

        assertEquals(expectedBookingObject.get(0).getGuestInfo(), actualBookingObject.get(0).getGuestInfo(), 
        "tester om riktig gjesteinfromasjon blir lest fra fil");

        // booking2.setBookedRoomID(room2);
        assertEquals(expectedBookingObject.get(1).getBookedRoomID(), actualBookingObject.get(1).getBookedRoomID(),
                "tester om booked RoomID er det samme som i fil");
 
        assertEquals(expectedBookingObject.get(1).getTotalPrice(), actualBookingObject.get(1).getTotalPrice(),
        "tester om riktig pris blir kalulert fra fil");
        

        Iterator<Booking> expectedIterator = expectedBookingObject.iterator();
        Iterator<Booking> actualIterator = actualBookingObject.iterator();

        while (expectedIterator.hasNext()) {
            try {
                Booking expectedBooking = expectedIterator.next();
                Booking actualBooking = actualIterator.next();
                assertEquals(expectedBooking.getbookedDates(), actualBooking.getbookedDates());
            } catch (IndexOutOfBoundsException e) {
                fail("The read list does not contain the correct number of items.");
            }
        }
    }

    @Test
    public void testLoadNonExistingFile() {
        assertThrows(
                IOException.class,
                () -> fileBookingHandler.readBooking("non_existing_file"),
                "IOException burde utløses hvis filen ikke eksisterer");
    }

    @Test
    public void testReadInvalidBooking() {
        assertThrows(
                ArrayIndexOutOfBoundsException.class,
                () -> fileBookingHandler.readBooking("invalid_booking"),
                "ArrayIndexOutOfBoundsException burde utløses hvis filen er invalid");
    }

    @Test
    public void testDeleteBookingFromFile() throws IOException {
        fileBookingHandler.deleteBookingFromFile("test_booking");
        File actual = fileBookingHandler.getBookingFile("test_booking");
        assertEquals(0, actual.length(), "lengden på filen burde være 0 når innhodlet fra den er slettet");
        

    }


    @AfterAll
    public void teardown() {
        fileBookingHandler.getBookingFile("new_booking").delete();
        fileBookingHandler.getBookingFile("test_booking").delete();
        fileBookingHandler.getBookingFile("invalid_booking").delete();
    }



}
