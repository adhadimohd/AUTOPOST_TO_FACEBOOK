package model;

import java.sql.*;

public class fbgroup {
    static int id;
    static String name;
    static String url;
    static int sent;
    static String connstring;
    static int isactive;
    static String fbId;

    public static String getFbId() {
        return fbId;
    }

    public static void setFbId(String fbId) {
        fbgroup.fbId = fbId;
    }



    public static int getIsactive() {
        return isactive;
    }

    public static void setIsactive(int isactive) {
        fbgroup.isactive = isactive;
    }



    public void setId(int id){
        this.id=id;
    }

    public int getId() {
        return this.id;
    }

    public void setSent(int sent){
        this.sent=sent;
    }

    public int getSent() {
        return this.sent;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getName(){
        return this.name;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getUrl(){
        return this.url;
    }

    public fbgroup ()
    {}

    public  fbgroup(int mId, String mName, String mUrl, int mSent, int isactive, String mfbId)
    {
        this.id = mId;
        this.name = mName;
        this.url = mUrl;
        this.sent = mSent;
        this.isactive=isactive;
        this.fbId=mfbId;
    }

    public  fbgroup(String mName)
    {
        this.name = mName;
    }

    public  fbgroup(int mId, String mName)
    {
        this.id = mId;
        this.name = mName;
    }


    public boolean SelectUnsend(){
        String sql = "SELECT * "
                + "FROM FBGroup WHERE sent = 0 limit 1";

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql))
        {

            // set the value
            //pstmt.setInt(1,status);
            //
            System.out.println(sql);
            ResultSet rs  = pstmt.executeQuery();


            boolean empty = true;
            while (rs.next()){

                this.id =rs.getInt("id");
                this.name = rs.getString("name");
                this.url = rs.getString("url");
                this.fbId = rs.getString("fbId");
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

    public void GetFBGroupbyname(String name)
    {
        String sql = "select * from fbgroup where name =?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql))
        {

            pstmt.setString(1, name);
            // System.out.println(sql);
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                //i++;

                this.id=rs.getInt("id");
                this.url=rs.getString("url");
                this.name=name;
                this.fbId=rs.getString("fbid");
                this.isactive=rs.getInt("isactive");

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void selectAll(){
        String sql = "SELECT *  capacity FROM FBGroup";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
            //     System.out.println(rs.getInt("id") +  "\t" +
            //                        rs.getString("name") + "\t" +
            //                       rs.getDouble("capacity"));

             }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void RefreshGroup() throws SQLException {

        String sql = "update fbgroup set isactive =0 where fbid not in (select fbid from fbgroupnewlist)";
        try  {


            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.executeUpdate();

            sql = "insert into fbgroup (name, url, sent, isactive, fbid) \n" +
                    " select name, url, sent, isactive, fbid from fbgroupnewlist where fbid not in (\n" +
                    " select fbid from fbgroup)";

            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();




        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }


    }


    public int Insert(String name, String url, String fbid)
    {
        String sql = "INSERT INTO FBGroupNewList (name,url,sent,isactive, fbId) VALUES(?,?,?,?,?)";

        this.name = name;
        this.url = url;
        this.fbId = fbid;

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, this.name);
            pstmt.setString(2, this.url);
            pstmt.setInt(3, 0);
            pstmt.setInt(4, 1);
            pstmt.setString(5, this.fbId);
            pstmt.executeUpdate();

            System.out.println(this.name + " saved");

            return 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }

    }

    public boolean Update(int id, String name, String url, int sent)
    {
        String sql = "Update FBGroup set name=?, url=?, sent=? WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            //System.out.println(sql);

            pstmt.setString(1, name);
            pstmt.setString(2, url);
            pstmt.setInt(3, sent);
            pstmt.setInt(4, id);

            // execute the delete statement
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean Delete(int id)
    {
        String sql = "DELETE FROM FBGroup WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, id);
            // execute the delete statement
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }


    }

    public boolean DeleteAllRecord()
    {
        String sql = "DROP TABLE FBGroup ";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // execute the delete statement
            pstmt.executeUpdate();
            System.out.println("All group record deleted");
            return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }


    }

    public boolean Select(int id)
    {
        return false;
    }

    public boolean SelectByUrl(String url)
    {
        return false;
    }


    public void initializeTable(String constr)
    {

        this.connstring = constr;
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS FBGroup (\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	name VARCHAR(128) NOT NULL,\n"
                + "	url VARCHAR(128),\n"
                + "	sent integer,\n"
                + "	isactive integer,\n"
                + "	fbid VARCHAR(80)  UNIQUE ON CONFLICT FAIL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(constr);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
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
