package prosjekt;


public class Room {
    private String roomName;
    private int guestCapasity; 
    private double price;
 
    


    public Room(String roomName, int guestCapasity, double price){
        this.roomName = roomName;
        this.guestCapasity= guestCapasity;
        this.price= price;
    }


    public Room() {
    }


    public String getRoomName() {
        return roomName;
    }
    
    public int getGuestCapasity() {
        return guestCapasity;
    }
    
    public double getPrice() {
        return price;
    }

    
    
    @Override
    public String toString() {
        return roomName + "\n" + "Guest capasity: " + guestCapasity + "\n" + "Price per night: " + price;
    }

    
}
