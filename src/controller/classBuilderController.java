package controller;

import com.sun.jna.platform.FileUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;


import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UnknownFormatConversionException;

/**
 * Created by merc on 9/27/2016.
 */
public class classBuilderController {
    @FXML private TextArea txtcode;
    @FXML protected void initialize() throws Exception
    {

        List<column> list = new ArrayList<column>();


        String classname = "PostPermalink";
        list.add(new column("id","int"));
        list.add(new column("permalink","String"));
        list.add(new column("datetime","int"));
        list.add(new column("fbid","String"));
        list.add(new column("postcontentid","int"));




        //template
        String t1="private [type] _[name];";
        String t2="public Simple[type]Property [name];";
        String t3="public [type] get[name]() {\n" +
                "if ([name]==null)\n" +
                "\treturn _[name]; \n" +
                "else \n" +
                "\treturn [name].get();\n" +
                "}\n" +
                "" +
                "public Simple[type]Property [name]Property() {\n" +
                "if ([name]==null) \n" +
                "\t[name]=new Simple[type]Property(this,\"[name]\",_[name]); \n" +
                "\treturn [name];\n" +
                "}\n" +
                "" +
                "public void set[name]([type] value) {\n" +
                "if ([name]==null) \n" +
                "\t_[name]=value; \n" +
                "else  \n" +
                "\t[name].set(value);\n" +
                "}\n";


        String sConnect="private Connection connect() {\n" +
                "\n" +
                "        Connection conn = null;\n" +
                "        try {\n" +
                "            conn = DriverManager.getConnection(\"jdbc:sqlite:autopost.db\");\n" +
                "            //System.out.println(\"Connection created\");\n" +
                "        } catch (SQLException e) {\n" +
                "            System.out.println(e.getMessage());\n" +
                "        }\n" +
                "        return conn;\n" +
                "    }";


        String t4 ="[name] = ?";
        String t5 ="pstmt.set[type]([count], this.get[name]());";
        String t6 ="public boolean save() {\n" +
                "        boolean result = false;\n" +
                "        String sql;\n" +
                "\n" +
                "        if (this.id==null )\n" +
                "        {\n" +
                "             sql = \"Insert into [classname] ([insertcolumn]) values  ([insertcolumnprep])\";\n" +
                "        }\n" +
                "        else {    " +
                "\n" +
                "         sql = \"Update [classname] set [tsqlsave] where id =?\";\n" +
                "\t}\n" +
                "\n" +
                "        try (Connection conn = this.connect();\n" +
                "             PreparedStatement pstmt = conn.prepareStatement(sql)) {\n" +
                "            [tsqlsaveprep]\n" +
                "            pstmt.executeUpdate();\n" +
                "            result =true;\n" +
                "\n" +
                "        } catch (SQLException e) {\n" +
                "            System.out.println(e.getMessage());\n" +
                "            result=false;\n" +
                "\n" +
                "        }\n" +
                "\n" +
                "        return result;\n" +
                "\n" +
                "    }\n";

        String t7 = " public void load(int mId)\n" +
                "    {\n" +
                "        String sql = \"SELECT * \"\n" +
                "                + \"FROM [classname] WHERE id=\" + mId;\n" +
                "        \n" +
                "        try (Connection conn = this.connect();\n" +
                "             PreparedStatement pstmt  = conn.prepareStatement(sql))\n" +
                "        {\n" +
                "            ResultSet rs  = pstmt.executeQuery();\n" +
                "\n" +
                "            boolean empty = true;\n" +
                "            while (rs.next()){\n" +
                "\n" +
                "[tsqlload]" +
                "\n" +
                "\n" +
                "            }\n" +
                "            \n" +
                "        } catch (SQLException e) {\n" +
                "            System.out.println(e.getMessage());\n" +
                "        }\n" +
                "\n" +
                "    }";

        String t10 = "\t\tthis.set[name](rs.get[type](\"[name]\"));";

        String b1 ="";
        String b2="";
        String b3="";
        String b4="";
        String b5="";
        String b6 ="";
        String b7="";
        String b8="";
        String b9 = "";



        String ClassHeader="import javafx.beans.property.SimpleIntegerProperty;\n" +
                "import javafx.beans.property.SimpleStringProperty;\n" +
                "\n" +
                "import java.sql.*;" +
                "import java.sql.Connection;\n" +
                "import java.sql.DriverManager;\n" +
                "import java.sql.PreparedStatement;\n" +
                "import java.sql.SQLException;" +
                "\n\n\n" +
                "public class " + classname + " {\n";
        int count = 0;
        String semicolon =",";






        for (column c:list)
        {
            count++;

            //building properties for update
            b1 = b1 + "\n"+ t1.replace("[type]", c.gettype()).replace("[name]",c.getname());
            b2 = b2 + "\n"+ t2.replace("[type]", c.gettype()).replace("[name]",c.getname());
            b3 = b3 + "\n"+ t3.replace("[type]", c.gettype()).replace("[name]",c.getname());
            b9 = b9 + "\n"+ t10.replace("[type]", c.gettype()).replace("[name]",c.getname());
            b2 = b2.replace("Simpleint","SimpleInteger");
            b3 = b3.replace("Simpleint","SimpleInteger");

            //update
            if (count>1)
            {
                b4=b4 + t4.replace("[name]", c.getname());
                if(count==list.size())
                {
                    semicolon="";

                }
                b4=b4+semicolon;

                // param statement
                b5 = b5+"\n\t\t"+t5.replace("[type]", c.gettype()).replace("[name]",c.getname());
                b5 = b5.replace("[count]",String.valueOf(count-1));
                b5=b5.replace(".setint(",".setInt(");
                // insert sql string
                b7=b7 + c.getname() + semicolon;
                b8=b8+ "?"+semicolon;


            }
        }

        b5=b5+"\n\t\tif (this.id!=null) ";
        b5=b5+"\n\t\t\t"+t5.replace("[type]", "Int").replace("[name]","id").replace("[count]",String.valueOf(count));
        b5=b5.replace(".setint(",".setInt(");
        b9=b9.replace(".getint(",".getInt(");

        b6=t6.replace("[tsqlsave]", b4);
        b6=b6.replace("[tsqlsaveprep]", b5);
        b6=b6.replace("[insertcolumn]",b7);
        b6=b6.replace("[insertcolumnprep]",b8);
        b9=t7.replace("[tsqlload]",b9);



        String classFooter = "\n\n}";

        String finalClass = "";
        finalClass=ClassHeader + b1 +"\n\n"+ b2 +"\n\n"+ b3 + "\n" + b6 + b9 + "\n" + sConnect + classFooter;
        finalClass = finalClass.replace("[classname]", classname);

        txtcode.setText(finalClass);

        try (PrintStream out = new PrintStream(new FileOutputStream("codegen/"+classname+".java"))) {
            out.print(finalClass);
           // out.flush();
           // out.close();
        }



    }


    private class column
    {
        //private final
        private String _name;
        private String _type;
        public SimpleStringProperty name;
        public SimpleStringProperty type;


        public String getname() {if (name==null) return _name; else return name.get();}
        public SimpleStringProperty nameProperty() {if (name==null) name=new SimpleStringProperty(this,"name",_name); return name;}
        public void setname(String value) {if (name==null) _name=value; else  name.set(value);}

        public String gettype() {
            if (type==null)
                return _type;
            else
                return type.get();
        }

        public SimpleStringProperty typeProperty() {
            if (type==null)
                type=new SimpleStringProperty(this,"type",_type);
            return type;
        }

        public void settype(String value) {
            if (type==null)
                _type=value;
            else
                type.set(value);
        }


        public column(){}

        public column(String mname, String mtype)
        {
            if (name==null)
                _name=mname;
            else
                name.set(mname);

            if (type==null)
                _type=mtype;
            else
                type.set(mtype);
        }


    }

}
