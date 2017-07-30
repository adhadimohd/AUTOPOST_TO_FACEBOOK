import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class PostContent {

private int _id;
private String _permalink;
private int _datetime;
private String _fbid;
private int _postcontentid;


public SimpleIntegerProperty id;
public SimpleStringProperty permalink;
public SimpleIntegerProperty datetime;
public SimpleStringProperty fbid;
public SimpleIntegerProperty postcontentid;


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

public String getpermalink() {
if (permalink==null)
	return _permalink; 
else 
	return permalink.get();
}
public SimpleStringProperty permalinkProperty() {
if (permalink==null) 
	permalink=new SimpleStringProperty(this,"permalink",_permalink); 
	return permalink;
}
public void setpermalink(String value) {
if (permalink==null) 
	_permalink=value; 
else  
	permalink.set(value);
}

public int getdatetime() {
if (datetime==null)
	return _datetime; 
else 
	return datetime.get();
}
public SimpleIntegerProperty datetimeProperty() {
if (datetime==null) 
	datetime=new SimpleIntegerProperty(this,"datetime",_datetime); 
	return datetime;
}
public void setdatetime(int value) {
if (datetime==null) 
	_datetime=value; 
else  
	datetime.set(value);
}

public String getfbid() {
if (fbid==null)
	return _fbid; 
else 
	return fbid.get();
}
public SimpleStringProperty fbidProperty() {
if (fbid==null) 
	fbid=new SimpleStringProperty(this,"fbid",_fbid); 
	return fbid;
}
public void setfbid(String value) {
if (fbid==null) 
	_fbid=value; 
else  
	fbid.set(value);
}

public int getpostcontentid() {
if (postcontentid==null)
	return _postcontentid; 
else 
	return postcontentid.get();
}
public SimpleIntegerProperty postcontentidProperty() {
if (postcontentid==null) 
	postcontentid=new SimpleIntegerProperty(this,"postcontentid",_postcontentid); 
	return postcontentid;
}
public void setpostcontentid(int value) {
if (postcontentid==null) 
	_postcontentid=value; 
else  
	postcontentid.set(value);
}

public boolean save()
        boolean result = false;
        String sql;

        if (this.id==null )
        {
             sql = "Insert into PostContent (permalink,datetime,fbid,postcontentid) values  (?,?,?,?)";
        }
        else {    {
         sql = "Update PostContent set permalink = ?,datetime = ?,fbid = ?,postcontentid = ? where id =?";
	}

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
		pstmt.setString(1, this.getpermalink());
		pstmt.setInt(2, this.getdatetime());
		pstmt.setString(3, this.getfbid());
		pstmt.setInt(4, this.getpostcontentid());
		if (this.id!=null) 
			pstmt.setInt(5, this.getid());
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
            //System.out.println("Connection created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

}