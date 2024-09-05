package prosjekt;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Booking {

    private Room room;
    private ArrayList<String> guestInfo;
    private Guest guest;
    private String bookedRoomID;
    private double totalPrice;
    private Map<String, ArrayList<ArrayList<LocalDate>>> bookedDates = new HashMap<String, ArrayList<ArrayList<LocalDate>>>(); 
    private double discount = 1;


    public Booking(Guest guest, String bookedRoomID, double totalPrice){
        this.totalPrice = totalPrice;
        this.guest = guest;
        this.bookedRoomID = bookedRoomID;
        setGuestInfo(guest);
    }

    public Booking(){

    }
    

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public ArrayList<String> getGuestInfo() {
        return guestInfo;
    }

    //Setter informajson fra et guest-objekt inn i liste
    public void setGuestInfo(Guest guest) {
        this.guestInfo = new ArrayList<String>();
        guestInfo.addAll(List.of(guest.getFullName(), guest.getEmail(), String.valueOf(guest.getTotalGuests()),
        String.valueOf(guest.getCheckIn()), String.valueOf(guest.getCheckOut())));

    }

    
    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Guest getGuest() {
        return guest;
    }

    private boolean checkValidDates(Guest guest){
        boolean isValid = true;
        for (ArrayList<LocalDate> invalidDates : bookedDates.get(getBookedRoomID())) {
            if (guest.getCheckIn().isAfter(invalidDates.get(0).minusDays(1)) 
            && guest.getCheckIn().isBefore(invalidDates.get(1)) ||
            guest.getCheckIn() == invalidDates.get(0) || 
            guest.getCheckOut().isAfter(invalidDates.get(0)) && 
            guest.getCheckOut().isBefore(invalidDates.get(1).plusDays(1))){
                isValid = false;
            }              
        }
        return isValid;
        
    }

    private boolean checkValidGuests(Guest guest, Room room){
        boolean isValid = true;
        if (guest.getTotalGuests() > room.getGuestCapasity()){
            isValid = false;
        }
        return isValid;
    }

    public void setBookedRoomID(Room room){
        this.bookedRoomID = room.getRoomName();
    }

    public String getBookedRoomID(){
        return bookedRoomID;
    }


    public void bookRoom(Room room, Guest guest) {
        //Sjekk om rommet kan bookes
        if(!checkValidGuests(guest, room)){
            throw new IllegalArgumentException("Dette rommet har ikke plass til så mange gjester");
        }

        if (!bookedDates.containsKey(getBookedRoomID())&& checkValidGuests(guest, room)){
            bookedDates.put(getBookedRoomID(), new ArrayList<ArrayList<LocalDate>>());
            bookedDates.get(getBookedRoomID()).add(guest.getListOfBookedDates());
        } else {
            if (checkValidDates(guest) && checkValidGuests(guest, room)){
                bookedDates.get(getBookedRoomID()).add(guest.getListOfBookedDates());
            } else{
                throw new IllegalArgumentException("Rommet kan ikke bookes innen disse datoene");
            } 
        }
        

    }

    public void addBookedDatesFromFile(String bookedRoomID, ArrayList<LocalDate> dates){ //brukes bare ved lesing fra fil i konstruktør
        if (!bookedDates.containsKey(bookedRoomID)){
            bookedDates.put(bookedRoomID, new ArrayList<ArrayList<LocalDate>>());
            bookedDates.get(bookedRoomID).add(dates);
        } else {
            bookedDates.get(bookedRoomID).add(dates);
        }   

    }

    public Map<String, ArrayList<ArrayList<LocalDate>>> getbookedDates(){
        return bookedDates;
    }

    public void cancelReservation(String bookedRoomID, ArrayList<LocalDate> dates){
        bookedDates.get(bookedRoomID).remove(dates);

    }

    


    //Kode hentet fra AI-tool, fikk hjelp av Børge
    public long getNights(LocalDate checkIn, LocalDate checkOut){
        LocalDate inDate = LocalDate.of(checkIn.getYear(), checkIn.getMonthValue(), checkIn.getDayOfMonth());
        LocalDate outDate = LocalDate.of(checkOut.getYear(), checkOut.getMonthValue(), checkOut.getDayOfMonth());
        long daysBetween = ChronoUnit.DAYS.between(inDate, outDate);
        return daysBetween;

    }

    private double calculateDiscount(Integer kids){
        if (kids == 0){
            discount = 1;
        } else if (kids == 1){
            discount = 0.9;
        } else if (kids == 2){
            discount = 0.85;
        }else {
            discount = 0.8;
        }
        return discount;
        
    }
        


    public double setTotalPrice(Room room, LocalDate checkIn, LocalDate checkOut, int kids){
        totalPrice = room.getPrice() * getNights(checkIn, checkOut) * calculateDiscount(kids);
        return totalPrice;  

    }

    public double getTotalPrice(){
        return totalPrice;
    }




    

    


       
}
