package prosjekt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class HotelBookingController {
    private Booking booking;
    private Guest guest;
    private List<Room> roomOverview;
    private FileBookingHandler bookingsFile = new FileBookingHandler();

    @FXML 
    private DatePicker checkInDatePicker, checkOutDatePicker;

    @FXML
    private TextField firstNameTextField, lastNameTextField, emailTextField;

    @FXML 
    private Button cancelButton, bookRoomButton, checkRoomsButton, loadButton, saveButton;

    @FXML
    private ChoiceBox<Integer> adultsChoiceBox, kidsChoiceBox;

    private Integer[] kids = {0,1,2,3};
    private Integer[] adults = {1,2,3,4};

    @FXML
    private Pane pane1;

    @FXML
    private Text hotelBooking;

    @FXML
    private Tab bookingsOverviewTab;

    @FXML
    private TableView<Booking> bookingsTableView;

    @FXML
    private TableColumn<Booking, String> nameColumn, emailColumn, checkInColumn, checkOutColumn, bookedRoomColumn;

    @FXML
    private TableColumn<Booking, Number> guestsColumn;

    @FXML
    private TableColumn<Booking, Number> totalPriceColumn;

    @FXML
    private GridPane RoomOverviewGridPane;

    @FXML
    private Label priceLabel;

    

    @FXML
    private void initialize() {
        booking = new Booking();
        guest = new Guest();
        
        adultsChoiceBox.getItems().addAll(adults);
        kidsChoiceBox.getItems().addAll(kids);
        

        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getGuest().getFullName()));
        emailColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getGuest().getEmail())); 
        checkInColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getGuest().getCheckIn().toString()));
        checkOutColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getGuest().getCheckOut().toString()));
        bookedRoomColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getBookedRoomID()));  
        guestsColumn.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(cellData.getValue().getGuest().getTotalGuests()));
        totalPriceColumn.setCellValueFactory(cellData -> new ReadOnlyDoubleWrapper(cellData.getValue().getTotalPrice()));

        initializeRoomOverviewList();

        for (int i = 0; i < roomOverview.size(); i++) {
            Room room = roomOverview.get(i);
            RoomOverviewGridPane.add(createRoomButton(room), i % 4, i / 4);
            
        }

        bookRoomButton.setDisable(true);
        saveButton.setDisable(true);
        checkRoomsButton.setDisable(true);
        cancelButton.setDisable(true);


    }

    

    @FXML 
    private void checkRooms(ActionEvent event){
        //Henter input far brukeren og setter verdier til Guest

        try {
            try {
                LocalDate checkIn = checkInDatePicker.getValue();
                LocalDate checkOut = checkOutDatePicker.getValue();
                guest.setCheckInDate(checkIn);
                guest.setCheckOutDate(checkOut);
                
                
            } catch (IllegalArgumentException e) {
                showErrorMessage(e.getMessage());
            }
    
            
            try {
                int adults = adultsChoiceBox.getValue();
                int kids = kidsChoiceBox.getValue();
                guest.setAdults(adults);
                guest.setKids(kids);
                guest.setTotalGuests(adults, kids);
                
            } catch (NullPointerException e) {
                showErrorMessage("Vennligst angi antall vokse og barn");
            }
            
            
            try {
                String firstName = firstNameTextField.getText();
                String lastName = lastNameTextField.getText();
                guest.setFirstName(firstName);
                guest.setLastName(lastName);
                guest.setFullName(firstName, lastName);
                
            } catch (Exception e) {
                showErrorMessage(e.getMessage());
            }
            
            
            try {
                String email = emailTextField.getText();
                guest.setEmail(email);
                
            } catch (IllegalArgumentException e) {
                showErrorMessage(e.getMessage());
            }
        } catch (RuntimeException e) {
            showErrorMessage("Vennligst fyll inn gjesteinformasjon først");
        }


        bookRoomButton.setDisable(false);

        booking.setGuestInfo(guest);
        
    }

    

    @FXML
    private void bookRoom(ActionEvent event){

        Room room = booking.getRoom();

        Guest guest = new Guest(firstNameTextField.getText() + " " + lastNameTextField.getText(), emailTextField.getText(),
             adultsChoiceBox.getValue() + kidsChoiceBox.getValue(), checkInDatePicker.getValue(), checkOutDatePicker.getValue());

        guest.setListOfBookedDates(checkInDatePicker.getValue(), checkOutDatePicker.getValue());

        guest.setTotalGuests(adultsChoiceBox.getValue(), kidsChoiceBox.getValue());

        String roomID = booking.getBookedRoomID(); 
        
        double totalPrice = booking.getTotalPrice();

        Booking newBooking = new Booking(guest, roomID, totalPrice);

        ObservableList<Booking> bookingsTabView = bookingsTableView.getItems();
        

        bookRoomButton.setDisable(true);

        
        try {
            booking.bookRoom(room, guest); 
            bookingsTabView.add(newBooking);
            bookingsTableView.setItems(bookingsTabView);
            
        } catch (IllegalArgumentException e) {
            showErrorMessage(e.getMessage());
            
        }
         

    }


    @FXML
    private void cancelReservation(ActionEvent event){
        int selectedBooking = bookingsTableView.getSelectionModel().getSelectedIndex();
        String bookedRoomID = bookingsTableView.getItems().get(selectedBooking).getBookedRoomID();
        ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
        dates.add(bookingsTableView.getItems().get(selectedBooking).getGuest().getCheckIn());
        dates.add(bookingsTableView.getItems().get(selectedBooking).getGuest().getCheckOut());

        booking.cancelReservation(bookedRoomID, dates);
        
        bookingsTableView.getItems().remove(selectedBooking);

    }


    @FXML
    private void handleLoad(ActionEvent event){

        try {
            ObservableList<Booking> bookingsTabViewFromFile = FXCollections.observableArrayList(bookingsFile.readBooking("bookingsFile"));
            bookingsTableView.setItems(bookingsTabViewFromFile);
            try{   
                for (Booking bookingInfo : bookingsTabViewFromFile) {
                    String bookedRoomID =bookingInfo.getBookedRoomID();
                    ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
                    dates.add(bookingInfo.getGuest().getCheckIn());
                    dates.add(bookingInfo.getGuest().getCheckOut());
                    booking.addBookedDatesFromFile(bookedRoomID, dates);
                    
                }
            } catch (Exception e){
                showErrorMessage(e.getMessage());
            }


        } catch (Exception e) {
            showErrorMessage(e.getMessage());
        }

        saveButton.setDisable(false);
        checkRoomsButton.setDisable(false);
        cancelButton.setDisable(false);
        

    }

    @FXML
    private void handleSave(ActionEvent event){
        
        try {
            bookingsFile.deleteBookingFromFile("bookingsFile");
            for (Booking booking1 : bookingsTableView.getItems()){ 
                bookingsFile.writeBooking("bookingsFile", booking1);    
            }
            showMessage("Bookingene er lagret til filen: bookingsFile.txt");
        } catch (Exception e) {
            showErrorMessage(e.getMessage());
        }
    }



    private Button createRoomButton(Room room){
        Button button = new Button(room.toString());
        button.setOnAction((event) -> handleRoomSelect(room));
        
        button.setStyle("-fx-text-alignment: center;");
        button.setStyle("-fx-background-color:  #f0dddd");
        button.setCursor(Cursor.HAND);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMaxHeight(Double.MAX_VALUE);
        RoomOverviewGridPane.setHgap(2);
        RoomOverviewGridPane.setVgap(3);
        RoomOverviewGridPane.setPadding(new Insets(5,1,5,5));

        
        return button;
    }


    private void handleRoomSelect(Room room){

        LocalDate checkIn = checkInDatePicker.getValue();
        LocalDate checkOut = checkOutDatePicker.getValue();
        int kids = kidsChoiceBox.getValue();
        booking.setTotalPrice(room, checkIn, checkOut, kids);
        

        priceLabel.setText(String.valueOf(booking.getTotalPrice()+ " " + "kr"));

        booking.setRoom(room);
        booking.setBookedRoomID(room);

    }
    

    


    //Håndtering av unntak, popper opp feilmelding i appen når noe går galt
    private void showErrorMessage(String message){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();

    }

    //metode for å gi informasjon til bruker
    private void showMessage(String message){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText(message);
        alert.showAndWait();
        
    }

    
   

    private void initializeRoomOverviewList(){
        roomOverview = new ArrayList<>();
        roomOverview.addAll(List.of(
            new Room("Single room 1", 1, 950),
            new Room("Singel room 2", 1, 1000),
            new Room("Twin room 1", 2, 1200),
            new Room("Twin room 2", 2, 1200),
            new Room("Double room 1", 2, 1400),
            new Room("Double room 2", 3, 1400),
            new Room("Double room 3", 3, 1400), 
            new Room("Family room 1", 4, 2200), 
            new Room("Family room 2", 5, 2200), 
            new Room("Family Suit", 5, 4000), 
            new Room("Queen Suit", 3, 3500), 
            new Room("King Suit", 3, 3800)));
        

    }

}

  

    





   
    





    

    
    

