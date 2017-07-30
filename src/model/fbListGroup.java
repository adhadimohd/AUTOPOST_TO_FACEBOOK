package model;/**
 * Created by merc on 9/19/2016.
 */

import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.*;
import java.util.List;

public class fbListGroup {

    static int id;
    static String listName;

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        fbListGroup.id = id;
    }

    public static String getListName() {
        return listName;
    }

    public static void setListName(String listName) {
        fbListGroup.listName = listName;
    }


    public void GetFbListGroupByName(String mName)
    {
        String sql = "select * from listgroup where Listname =?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql))
        {

            pstmt.setString(1, mName);
            // System.out.println(sql);
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {

                this.id=rs.getInt("id");
                this.listName =rs.getString("Listname");

            };

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }



    private Connection connect() {
        // SQLite connection string

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:autopost.db");
            //System.out.println("Connection created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

}