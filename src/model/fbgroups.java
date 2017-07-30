package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by merc on 9/20/2016.
 */
public class fbgroups {

private int rowcount;

    public int getRowcount() {
        return rowcount;
    }

    public void setRowcount(int rowcount) {
        this.rowcount = rowcount;
    }

    public ObservableList<fbgroup> GetAllGroup()
{
    ObservableList<fbgroup> data = FXCollections.observableArrayList();
    fbgroup row = new fbgroup();


    String sql = "select * from fbgroup";
    int i=0;

    try (Connection conn = this.connect();
         Statement stmt  = conn.createStatement();
         ResultSet rs    = stmt.executeQuery(sql)){

        // loop through the result set
        while (rs.next()) {
            i++;
            row = new fbgroup(rs.getInt("id"),rs.getString("name"));

           data.add(row);
            //System.out.println(rs.getString("name"));
            //System.out.println(row.getName());
        };

       // data=FXCollections.observableArrayList(rs);
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }

    this.rowcount=i;

    return data;

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
