package prosjekt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HotelBookingApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Hotel Booking");
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("HotelBookingApp.fxml"))));
        primaryStage.show();
    } 
    
    public static void main(String[] args) {
        Application.launch(args);
    }

}



