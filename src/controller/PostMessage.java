package controller;/**
 * Created by merc on 9/26/2016.
 */

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import model.PostContent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class PostMessage extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{


        ObservableList<PostContent> data = FXCollections.observableArrayList();
        TableView<PostContent> tv = new TableView<>(data);

        stage.setScene(new Scene(tv,900,500));
        stage.show();
        LoadPersonTable(tv,data);
    }


    private void LoadPersonTable(TableView<PostContent> tv, ObservableList<PostContent> data)
    {


        TableColumn id = new TableColumn("ID");
        id.setCellValueFactory(new PropertyValueFactory<PostContent,Integer>("id"));
        TableColumn description = new TableColumn("description");
        description.setCellValueFactory(new PropertyValueFactory<PostContent,String>("description"));
        TableColumn textmessage = new TableColumn("textmessage");
        textmessage.setCellValueFactory(new PropertyValueFactory<PostContent,String>("textmessage"));


        // tv.getColumns().addAll(id,name,epoch,tstamp);
        tv.getColumns().addAll(id,description,textmessage);

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:autopost.db");
             Statement stmt = con.createStatement();) {

            ResultSet rs = stmt.executeQuery("SELECT * FROM postcontent");
            while (rs.next()) {
                data.add(new PostContent(rs.getInt("id"),
                        rs.getString("description"),
                        rs.getString("textmessage").substring(0,50)+"...",
                        rs.getString("mediaurl1"),
                        rs.getString("mediaurl2"),
                        rs.getString("mediaurl3"),
                        rs.getInt("isactive")));
            }
            rs.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
}
