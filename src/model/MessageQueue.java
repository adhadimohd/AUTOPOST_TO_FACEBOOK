package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by merc on 9/21/2016.
 */
public class MessageQueue {

    private SimpleIntegerProperty id;
    private SimpleStringProperty fbid;
    private SimpleStringProperty textMessage;
    private SimpleIntegerProperty sent;
    private SimpleIntegerProperty processed;
    private SimpleIntegerProperty status;
    private SimpleIntegerProperty dateprocess;
    private SimpleIntegerProperty campaignid;
    private PostContent postcontent;
    private SimpleIntegerProperty postcontentid;

    public PostContent getPostcontent()
    {
        return this.postcontent;
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id=new SimpleIntegerProperty(id);
    }


    public int getpostcontentid() {
        return postcontentid.get();
    }

    public SimpleIntegerProperty postcontentidProperty() {
        return postcontentid;
    }

    public void setpostcontentid(int value) {
        this.postcontentid=new SimpleIntegerProperty(value);
    }





    public String getFbid() {
        return fbid.get();
    }

    public SimpleStringProperty fbidProperty() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid=new SimpleStringProperty(fbid);
    }

    public String getTextMessage() {

        String sql = "select * from Postcontent where id =?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql))
        {

            pstmt.setInt(1, this.getpostcontentid());
            // System.out.println(sql);
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {

                this.setTextMessage(rs.getString("textmessage"));
                this.postcontent = new PostContent();
                this.postcontent.setid(this.postcontentid.get());
                this.postcontent.settextmessage(rs.getString("textmessage"));
                this.postcontent.setmediaurl1(rs.getString("mediaurl1"));
                this.postcontent.setmediaurl2(rs.getString("mediaurl2"));
                this.postcontent.setmediaurl3(rs.getString("mediaurl3"));
            };

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return textMessage.get();
    }

    public SimpleStringProperty textMessageProperty() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage=new SimpleStringProperty(textMessage);
    }

    public int getSent() {
        return sent.get();
    }

    public SimpleIntegerProperty sentProperty() {
        return sent;
    }

    public void setSent(int sent) {
        this.sent=new SimpleIntegerProperty(sent);
    }

    public int getProcessed() {
        return processed.get();
    }

    public SimpleIntegerProperty processedProperty() {
        return processed;
    }

    public void setProcessed(int processed) {
        this.processed=new SimpleIntegerProperty(processed);
    }

    public int getStatus() {
        return status.get();
    }

    public SimpleIntegerProperty statusProperty() {
        return status;
    }

    public void setStatus(int status) {
        this.status=new SimpleIntegerProperty(status);
    }



    public int getCampaignid() {
        return campaignid.get();
    }

    public SimpleIntegerProperty campaignidProperty() {
        return campaignid;
    }

    public void setCampaignid(int campaignid) {
        this.campaignid=new SimpleIntegerProperty(campaignid);
    }

    public int getDateprocess() {
        return dateprocess.get();
    }

    public SimpleIntegerProperty dateprocessProperty() {
        return dateprocess;
    }

    public void setDateprocess(int dateprocess) {
        this.dateprocess=new SimpleIntegerProperty(dateprocess);
    }


    public int GetCurrentTotalQueue() {
        String sql = "SELECT count(id) as total FROM messagequeue where sent=0";
        int total = 0;

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());


        }
        return total;
    }



    public boolean GetMesssageInQueue()
    {
        String sql = "SELECT * "
                + "FROM MessageQueue WHERE sent = 0 limit 1";

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql))
        {

            // set the value
            //pstmt.setInt(1,status);
            //
            //System.out.println(sql);
            ResultSet rs  = pstmt.executeQuery();

            boolean empty = true;
            while (rs.next()){

                this.setId(rs.getInt("id"));

                this.setTextMessage(rs.getString("textmessage"));
                this.setFbid(rs.getString("fbId"));

                this.setCampaignid(rs.getInt("campaignid"));
                this.setpostcontentid(rs.getInt("postcontentid"));

                this.setSent(0);

                empty=false;
                return true;
            }
            if (empty){
                return false;
            }




        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }


    // save message queue
    public boolean Save()
    {
        boolean result = false;
        //

        String sql = "Update MessageQueue set campaignid = ?, textmessage=?, fbid=?, sent=? where id =?";


        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, this.getCampaignid());
            pstmt.setString(2, this.getTextMessage());
            pstmt.setString(3, this.getFbid());
            pstmt.setInt(4, this.getSent());
            pstmt.setInt(5, this.getId());
            pstmt.executeUpdate();
            result =true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            result=false;

        }

        return result;

    }


    public void UpdateMQContent()
    {
        String sql = "update messagequeue set postcontentid = (select id from postcontent where isactive=1 order by random() limit 1) " +
                " where id in (select id from messagequeue where postcontentid is null order by random() limit 10)";


        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);


            for (int i=1;i<20;i++) {

                pstmt.execute();

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(sql);
        }
    }


    public void CreateMQ() {
        String sql = "insert into messagequeue (campaignid, fbid, sent) select 1 as campaignid,  listgroupdetail.fbid,  0 as sent from listgroupdetail where listgroupid = 1";


        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.execute();


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(sql);
        }
    }

    public void ClearMQ() {
        String sql = "delete from messagequeue ";


        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.execute();


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(sql);
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
