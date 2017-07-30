package controller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.PostContent;
import model.PostPermalink;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by merc on 9/30/2016.
 */
public class PostPermalinkMain extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        ObservableList<PostPermalink> data = FXCollections.observableArrayList();
        TableView<PostPermalink> tv= new TableView<>(data);

        stage.setScene(new Scene(tv,500,500));
        stage.show();

        LoadPermalinkData(tv, data);

    }

    public void LoadPermalinkData(TableView<PostPermalink> tv, ObservableList<PostPermalink> data)
    {

//        TableColumn id = new TableColumn("ID");
//        id.setCellValueFactory(new PropertyValueFactory<PostPermalink,Integer>("id"));
//        TableColumn description = new TableColumn("description");
//        description.setCellValueFactory(new PropertyValueFactory<PostPermalink,String>("description"));
//        TableColumn textmessage = new TableColumn("textmessage");
//        textmessage.setCellValueFactory(new PropertyValueFactory<PostPermalink,String>("textmessage"));
//        // tv.getColumns().addAll(id,name,epoch,tstamp);
//        tv.getColumns().addAll(id,description,textmessage);
//
//        try (Connection con = DriverManager.getConnection("jdbc:sqlite:autopost.db");
//             Statement stmt = con.createStatement();) {
//
//            ResultSet rs = stmt.executeQuery("SELECT * FROM postcontent");
//            while (rs.next()) {
//                data.add(new PostContent(rs.getInt("id"),
//                        rs.getString("description"),
//                        rs.getString("textmessage").substring(0,50)+"...",
//                        rs.getString("mediaurl1"),
//                        rs.getString("mediaurl2"),
//                        rs.getString("mediaurl3"),
//                        rs.getInt("isactive")));
//            }
//            rs.close();
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            System.exit(0);
//        }
    }


    public static void main()
    {
        launch();
    }
}
