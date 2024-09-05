package prosjekt;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface IBookingsHandler {
    
    List<Booking> readBooking(String filename) throws IOException;

    void writeBooking(String filename, Booking booking) throws IOException;

    File getBookingFile(String filename);
}
