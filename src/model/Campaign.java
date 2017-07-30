package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * Created by merc on 9/22/2016.
 */
public class Campaign {

    private int _id;
    private String _name;
    private int _dateCreated;
    private int _listGroupId;
    private String _textMessage;
    private int _QueueCreated;

    public SimpleIntegerProperty id;
    public SimpleStringProperty name;
    public SimpleIntegerProperty dateCreated;
    public SimpleIntegerProperty QueueCreated;
    public SimpleIntegerProperty listGroupId;
    public SimpleStringProperty textMessage;

    public String gettextMessage() {
        if (textMessage==null)
            return _textMessage;
        else
            return textMessage.get();
    }

    public SimpleStringProperty textMessageProperty() {
        if (textMessage==null)
            textMessage=new SimpleStringProperty(this,"textMessage",_textMessage);
        return textMessage;
    }

    public void settextMessage(String value) {
        if (textMessage==null)
            _textMessage=value;
        else
            textMessage.set(value);
    }


    public int getQueueCreated() {
        if (QueueCreated==null)
            return _QueueCreated;
        else
            return QueueCreated.get();
    }
    public int getlistGroupId() {
        if (listGroupId==null)
            return _listGroupId;
        else
            return listGroupId.get();
    }

    public SimpleIntegerProperty listGroupIdProperty() {
        if (listGroupId==null)
            listGroupId=new SimpleIntegerProperty(this,"listGroupId",_listGroupId);
        return listGroupId;
    }

    public void setlistGroupId(int value) {
        if (listGroupId==null)
            _listGroupId=value;
        else
            listGroupId.set(value);
    }

    public SimpleIntegerProperty QueueCreatedProperty() {
        if (QueueCreated==null)
            QueueCreated=new SimpleIntegerProperty(this,"QueueCreated",_QueueCreated);
        return QueueCreated;
    }

    public void setQueueCreated(int value) {
        if (QueueCreated==null)
            _QueueCreated=value;
        else
            QueueCreated.set(value);
    }

    public String getname() {
        if (name==null)
            return _name;
        else
            return name.get();
    }

    public SimpleStringProperty nameProperty() {
        if (name==null)
            name=new SimpleStringProperty(this,"name",_name);
        return name;
    }

    public void setname(String value) {
        if (name==null)
            _name=value;
        else
            name.set(value);
    }



    public int getdateCreated() {
        if (dateCreated==null)
            return _dateCreated;
        else
            return dateCreated.get();
    }

    public SimpleIntegerProperty dateCreatedProperty() {
        if (dateCreated==null)
            dateCreated=new SimpleIntegerProperty(this,"dateCreated",_dateCreated);
        return dateCreated;
    }

    public void setdateCreated(int value) {
        if (dateCreated==null)
            _dateCreated=value;
        else
            dateCreated.set(value);
    }


    public int getid() {
        if (id==null)
            return _id;
        else
            return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        if (id==null)
            id=new SimpleIntegerProperty(this,"id",_id);
        return id;
    }

    public void setid(int value) {
        if (id==null)
            _id=value;
        else
            id.set(value);
    }



    public void load(int id)
    {
        String sql = "select * from campaign where id =?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql))
        {

            pstmt.setInt(1, id);
            // System.out.println(sql);
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {

                this.setid(id);
                this.setname(rs.getString("name"));
                this.setlistGroupId(rs.getInt("listgroupid"));
                this.setQueueCreated(rs.getInt("queuecreated"));
                this.settextMessage(rs.getString("textmessage"));

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

    }

    public int GetTotalQueue() {
        String sql = "SELECT count(id) as total FROM messagequeue where campaignid=?";
        int total = 0;

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql))
        {

            pstmt.setInt(1, this.getid());
            // System.out.println(sql);
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                total = rs.getInt("total");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return total;
    }

    public void CreateMessageQueue()
    {
        // check campaign id
        String sql;
        sql= "insert into messagequeue ";
        sql=sql + "(campaignid, textmessage, fbid, sent ";
        sql=sql + "     )";
        sql=sql + " select ? as campaignid, ? as textmesssage, listgroupdetail.fbid, 0 as sent";
        sql=sql + " from listgroupdetail where listgroupid =?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, this.getid());
            pstmt.setString(2, this.gettextMessage());
            pstmt.setInt(3, this.getlistGroupId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void ClearAllQueue()
    {
        // check campaign id
        String sql;
        sql= "delete from messagequeue where campaignid=1";


        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            //pstmt.setInt(1, this.getid());
            pstmt.executeUpdate();
            //System.out.println(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }



    public boolean save() {
        boolean result = false;
        //
        String sql = "Update Campaign set name = ?, queuecreated=?, listgroupid=?, textmessage=? where id =?";


        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, this.getname());
            pstmt.setInt(2, this.getQueueCreated());
            pstmt.setInt(3, this.getlistGroupId());
            pstmt.setString(4, this.gettextMessage());
            pstmt.setInt(5, this.getid());
            pstmt.executeUpdate();
            result =true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return result;
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
