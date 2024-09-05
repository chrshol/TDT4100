package prosjekt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;







public class FileBookingHandler implements IBookingsHandler {


    @Override
    public List<Booking> readBooking(String filename) throws IOException {

        List<Booking> bookings = new ArrayList<>();

        try(Scanner scanner = new Scanner(getBookingFile(filename))){
    
           
            while(scanner.hasNextLine()){
                
                
                String[] properties = scanner.nextLine().split(";");

                String[] guestProperties = properties[0].replace("[", "").replace("]", "").split(",");
                
                Guest guest = new Guest(guestProperties[0], guestProperties[1].strip(), Integer.valueOf(guestProperties[2].strip()), LocalDate.parse(guestProperties[3].strip()), LocalDate.parse(guestProperties[4].strip()));
                String bookedRoomID = properties[1];
                Double totalPrice = Double.valueOf(properties[2]);
                bookings.add(new Booking(guest, bookedRoomID, totalPrice));

            }
            
        }

        return bookings;
    }

    


    @Override
    public void writeBooking(String filename, Booking booking) throws IOException {
        
        try{
            FileWriter fileW = new FileWriter(getBookingFile(filename), true);
            PrintWriter outFile = new PrintWriter(fileW);
            
            outFile.println(String.format("%s;%s;%s",booking.getGuestInfo(),booking.getBookedRoomID(),booking.getTotalPrice())); 
           
            outFile.flush();
            outFile.close();
                
        } catch (IOException e){
            e.printStackTrace();
        }

    }


    public void deleteBookingFromFile(String filename) throws IOException{

        try{
            FileWriter writer = new FileWriter(getBookingFile(filename));
            writer.write("");
            

        } catch (IOException e){
            e.printStackTrace();

        }
    }




    @Override
    public File getBookingFile(String filename) {
        return new File(FileBookingHandler.class.getResource("bookings/").getFile() + filename + ".txt");
    }

    
}
