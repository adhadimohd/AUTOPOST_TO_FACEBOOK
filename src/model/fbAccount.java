package model;

import java.sql.*;

/**
 * Created by merc on 9/19/2016.
 */
public class fbAccount {
    static int Id;
    static String login;
    static String password;

    public static int getId() {
        return Id;
    }

    public static void setId(int id) {
        Id = id;
    }

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        fbAccount.login = login;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        fbAccount.password = password;
    }

    public void selectFirstId(){
        String sql = "SELECT *  FROM account";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
              while (rs.next()) {
                  fbAccount.Id=rs.getInt("id");
                  fbAccount.login=rs.getString("loginid");
                  fbAccount.password=rs.getString("password");
                 //System.out.println(rs.getInt("id") +  "\t" + rs.getString("loginid") + "\t" +rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //System.out.println(fbAccount.getId() + ":" + fbAccount.getLogin() + ":" + fbAccount.getPassword());

    }

    public void update(String login, String password)
    {
        String sql = "Update account set loginid=?, password=? WHERE id = 1";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            System.out.println(sql);

            pstmt.setString(1, login);
            pstmt.setString(2, password);

            // execute the delete statement
            pstmt.executeUpdate();


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
