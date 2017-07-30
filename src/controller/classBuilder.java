package controller;/**
 * Created by merc on 9/27/2016.
 */

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class classBuilder extends Application {



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/classbuilder.fxml"));
        Scene scene = new Scene(root);
       // scene.getStylesheets().add(getClass().getResource("/view/PostBuilderStyle.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("DehaFX - Code Builder");

       // String txt ="dadsa";
       // txt= txtcode.getText();
        stage.show();




    }
}
