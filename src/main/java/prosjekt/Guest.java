package prosjekt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Guest {
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int adults;
    private int kids;
    private int totalGuests;
    private ArrayList<LocalDate> listOfBookedDates;
    private static final List<String> countryCodes = Arrays.asList("ad", "ae", "af", "ag", "ai", "al", "am", "ao", "aq", "ar", "as", "at", "au", "aw", "ax", "az", "ba", "bb", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bl", "bm", "bn", "bo", "bq", "br", "bs", "bt", "bv", "bw", "by", "bz", "ca", "cc", "cd", "cf", "cg", "ch", "ci", "ck", "cl", "cm", "cn", "co","com", "cr", "cu", "cv", "cw", "cx", "cy", "cz", "de", "dj", "dk", "dm", "do", "dz", "ec", "ee", "eg", "eh", "er", "es", "et", "fi", "fj", "fk", "fm", "fo", "fr", "ga", "gb", "gd", "ge", "gf", "gg", "gh", "gi", "gl", "gm", "gn", "gp", "gq", "gr", "gs", "gt", "gu", "gw", "gy", "hk", "hm", "hn", "hr", "ht", "hu", "id", "ie", "il", "im", "in", "io", "iq", "ir", "is", "it", "je", "jm", "jo", "jp", "ke", "kg", "kh", "ki", "km", "kn", "kp", "kr", "kw", "ky", "kz", "la", "lb", "lc", "li", "lk", "lr", "ls", "lt", "lu", "lv", "ly", "ma", "mc", "md", "me", "mf", "mg", "mh", "mk", "ml", "mm", "mn", "mo", "mp", "mq", "mr", "ms", "mt", "mu", "mv", "mw", "mx", "my", "mz", "na", "nc", "ne", "nf", "ng", "ni", "nl", "no", "np", "nr", "nu", "nz", "om", "pa", "pe", "pf", "pg", "ph", "pk", "pl", "pm", "pn", "pr", "ps", "pt", "pw", "py", "qa", "re", "ro", "rs", "ru", "rw", "sa", "sb", "sc", "sd", "se", "sg", "sh", "si", "sj", "sk", "sl", "sm", "sn", "so", "sr", "ss", "st", "sv", "sx", "sy", "sz", "tc", "td", "tf", "tg", "th", "tj", "tk", "tl", "tm", "tn", "to", "tr", "tt", "tv", "tw", "tz", "ua", "ug", "um", "us", "uy", "uz", "va", "vc", "ve", "vg", "vi", "vn", "vu", "wf", "ws", "ye", "yt", "za", "zm", "zw");

    
    public Guest(String fullname, String email, int totalGuests, LocalDate checkIn, LocalDate checkOut) {
        this.fullName = fullname;
        this.email = email;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalGuests = totalGuests;
    }


    public Guest(){

    }


    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        checkValidFirstName(firstName);
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        checkValidLastName(lastName);
        this.lastName = lastName;
    }
     
    public String getFullName() {
        return fullName;
    }


    public void setFullName(String firstName, String lastName) {
        this.fullName = firstName + " " + lastName;
    }


    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        checkValidEmail(email);
        this.email = email;
    }

    private void checkValidEmail(String email) {
        String[] splitEmail = email.split("@");
        if (splitEmail.length != 2) {
            throw new IllegalArgumentException("Vennligst skriv inn gyldig email");
        }
        String[] domain = splitEmail[1].split("\\.");
    
        if (! countryCodes.contains(domain[1])) {
            throw new IllegalArgumentException("Vennligst skriv inn gyldig landekode i email");

        }
        
    }

    private void checkValidFirstName(String firstName){
        if (firstName.isBlank()){
            throw new IllegalArgumentException("Ugyldig navn");
        }
    }

    private void checkValidLastName(String lastName){
        if (lastName.isBlank()){
            throw new IllegalArgumentException("Ugyldig navn");
        }
    }

    public int getAdults() {
        return adults;
    }


    public void setAdults(int adults) {
        this.adults = adults;
    }


    public int getKids() {
        return kids;
    }


    public void setKids(int kids) {
        this.kids = kids;
    }

    public int getTotalGuests() {
        return totalGuests;
    }
    public void setTotalGuests(int adults, int kids) {
        this.totalGuests = adults + kids;
    }

    public void setCheckInDate(LocalDate checkIn) {
        if (!(checkIn.isAfter(LocalDate.now().minusDays(1)))){
            throw new IllegalArgumentException("Check-in må være før check-out og man kan ikke booke tilbake i tid");
        }
        this.checkIn = checkIn;  
    }
     

    
    public void setCheckOutDate(LocalDate checkOut) {
        if (!(checkOut.isAfter(this.checkIn) && checkOut.isAfter(LocalDate.now()))){
            throw new IllegalArgumentException("Check-out må være etter check-in");
        }
        this.checkOut = checkOut;
    }
     


    public LocalDate getCheckIn() {
        return checkIn;
    }


    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setListOfBookedDates(LocalDate checkIn, LocalDate checkOut){
        this.listOfBookedDates = new ArrayList<LocalDate>();
        listOfBookedDates.add(checkIn);
        listOfBookedDates.add(checkOut);
        
    }

    public ArrayList<LocalDate> getListOfBookedDates(){
        return listOfBookedDates;
    }

    

    
    
    





    

    
}
