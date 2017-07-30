package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;

import java.sql.*;


public class PostContent {

    private int _id;
    private String _description;
    private String _textmessage;
    private String _mediaurl1;
    private String _mediaurl2;
    private String _mediaurl3;
    private int _isactive;


    public SimpleIntegerProperty id;
    public SimpleStringProperty description;
    public SimpleStringProperty textmessage;
    public SimpleStringProperty mediaurl1;
    public SimpleStringProperty mediaurl2;
    public SimpleStringProperty mediaurl3;
    public SimpleIntegerProperty isactive;


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

    public String getdescription() {
        if (description==null)
            return _description;
        else
            return description.get();
    }
    public SimpleStringProperty descriptionProperty() {
        if (description==null)
            description=new SimpleStringProperty(this,"description",_description);
        return description;
    }
    public void setdescription(String value) {
        if (description==null)
            _description=value;
        else
            description.set(value);
    }

    public String gettextmessage() {
        if (textmessage==null)
            return _textmessage;
        else
            return textmessage.get();
    }
    public SimpleStringProperty textmessageProperty() {
        if (textmessage==null)
            textmessage=new SimpleStringProperty(this,"textmessage",_textmessage);
        return textmessage;
    }
    public void settextmessage(String value) {
        if (textmessage==null)
            _textmessage=value;
        else
            textmessage.set(value);
    }

    public String getmediaurl1() {
        if (mediaurl1==null)
            return _mediaurl1;
        else
            return mediaurl1.get();
    }
    public SimpleStringProperty mediaurl1Property() {
        if (mediaurl1==null)
            mediaurl1=new SimpleStringProperty(this,"mediaurl1",_mediaurl1);
        return mediaurl1;
    }
    public void setmediaurl1(String value) {
        if (mediaurl1==null)
            _mediaurl1=value;
        else
            mediaurl1.set(value);
    }

    public String getmediaurl2() {
        if (mediaurl2==null)
            return _mediaurl2;
        else
            return mediaurl2.get();
    }
    public SimpleStringProperty mediaurl2Property() {
        if (mediaurl2==null)
            mediaurl2=new SimpleStringProperty(this,"mediaurl2",_mediaurl2);
        return mediaurl2;
    }
    public void setmediaurl2(String value) {
        if (mediaurl2==null)
            _mediaurl2=value;
        else
            mediaurl2.set(value);
    }

    public String getmediaurl3() {
        if (mediaurl3==null)
            return _mediaurl3;
        else
            return mediaurl3.get();
    }
    public SimpleStringProperty mediaurl3Property() {
        if (mediaurl3==null)
            mediaurl3=new SimpleStringProperty(this,"mediaurl3",_mediaurl3);
        return mediaurl3;
    }
    public void setmediaurl3(String value) {
        if (mediaurl3==null)
            _mediaurl3=value;
        else
            mediaurl3.set(value);
    }

    public int getisactive() {
        if (isactive==null)
            return _isactive;
        else
            return isactive.get();
    }
    public SimpleIntegerProperty isactiveProperty() {
        if (isactive==null)
            isactive=new SimpleIntegerProperty(this,"isactive",_isactive);
        return isactive;
    }
    public void setisactive(int value) {
        if (isactive==null)
            _isactive=value;
        else
            isactive.set(value);
    }

    public PostContent(){}

    public PostContent(int mid, String mdescription, String mtextmessage, String mmediaurl1, String mmediaurl2, String mmediaurl3, int misactive)
    {
        this.setid(mid);
        this.setdescription(mdescription);
        this.settextmessage(mtextmessage);
        this.setmediaurl1(mmediaurl1);
        this.setmediaurl2(mmediaurl2);
        this.setmediaurl3(mmediaurl3);
        this.setisactive(misactive);
    }

    public void loadrandom()
    {
        String sql = "SELECT * "
                + "FROM Postcontent WHERE isactive=1 order by random() limit 1";

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql))
        {
            ResultSet rs  = pstmt.executeQuery();

            boolean empty = true;
            while (rs.next()){

                this.setid(rs.getInt("id"));
                this.setdescription(rs.getString("name"));
                this.settextmessage(rs.getString("url"));
                this.setmediaurl1(rs.getString("mediaurl1"));
                this.setmediaurl2(rs.getString("mediaurl2"));
                this.setmediaurl3(rs.getString("mediaurl3"));
                this.settextmessage(rs.getString("url"));

                this.setisactive(rs.getInt("isactive"));

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public boolean save()
    {
        boolean result = false;
        String sql;

        if (this.id==null )
        {
            sql = "Insert into PostContent (description,textmessage,mediaurl1,mediaurl2,mediaurl3,isactive) values  (?,?,?,?,?,?)";
        }
        else    {
            sql = "Update PostContent set description = ?,textmessage = ?,mediaurl1 = ?,mediaurl2 = ?,mediaurl3 = ?,isactive = ? where id =?";
        }

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, this.getdescription());
            pstmt.setString(2, this.gettextmessage());
            pstmt.setString(3, this.getmediaurl1());
            pstmt.setString(4, this.getmediaurl2());
            pstmt.setString(5, this.getmediaurl3());
            pstmt.setInt(6, this.getisactive());

            if (this.id!=null)
                pstmt.setInt(7, this.getid());
            pstmt.executeUpdate();
            result =true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            result=false;

        }

        return result;

    }

    private Connection connect() {

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:autopost.db");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

}