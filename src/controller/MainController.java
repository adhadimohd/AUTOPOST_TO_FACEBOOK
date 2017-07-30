package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import model.*;
import org.omg.CORBA.portable.UnknownException;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import java.io.IOException;
import java.sql.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class MainController {
    @FXML private TextField txtLogin;
    @FXML private TextField txtPassword;
    @FXML private TextField textCampaign;
    @FXML private TextArea txtMessage;
    @FXML private Button btnRunSender;
    //@FXML private Button btnClearMessageQueue;
    @FXML private Button btnCreateSendingList;
    @FXML private ListView<String> listView01;
    @FXML private ListView<String> listView02;
    @FXML private ChoiceBox<String> choicebox01;
    @FXML private ChoiceBox<String> choiceboxtarget02;
    @FXML private Label lblQueueCreated;
    @FXML private Label lblGroupInQueue;
    @FXML private TextField lblCampaignId;


    String prmLogin;
    String prmPassword;
    Timeline anime;
    WebDriver d;


    @FXML
    protected void initialize()
    {
        LoadCampaign();
        LoadAccount();
        LoadListViewGroup("");
        LoadGroupListInChoiceBox();

        listView02.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent click) {
                if(click.getButton().equals(MouseButton.PRIMARY)){
                    if(click.getClickCount() == 2){
                        //System.out.println("Double clicked");
                        String groupname = listView02.getSelectionModel().getSelectedItem();
                        try {
                        BrowseUrl(groupname);
                        }
                        catch (IOException e)
                        {
                            System.out.println(e.getMessage());
                        }

                    }
                }


            }

    });
    }

    public void BrowseUrl(String groupname) throws IOException {
        //System.out.println(fbid);
        fbgroup fb = new fbgroup();
        fb.GetFBGroupbyname(groupname);
        Runtime.getRuntime().exec(new String[]{"cmd", "/c","start chrome " + fb.getUrl()});

    }

    public void LoadCampaign()
    {
        //check last loaded campaign
        // no last loaded, just load top 1
        //no campaign, just empty


        Campaign c = new Campaign();

        int campaignId = 1;
        int listNo;

        c.load(campaignId);

        listNo=c.getlistGroupId()-1;
        textCampaign.setText(c.getname());
        txtMessage.setText(c.gettextMessage());

        lblCampaignId.setText(String.valueOf(campaignId));
        lblCampaignId.setVisible(false);

        // --- populate choiceboxcampaign
        String sql = "select Listname from ListGroup";

        ObservableList<String> data = FXCollections.observableArrayList();

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                data.add( rs.getString("Listname"));
            };

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        choiceboxtarget02.setItems(data);
        choiceboxtarget02.setTooltip(new Tooltip("Select List"));
        choiceboxtarget02.getSelectionModel().select(listNo);


        // end populate choicebox
        //System.out.println("Listno =" + listNo);




        MessageQueue q= new MessageQueue();
        lblGroupInQueue.setText(String.valueOf(q.GetCurrentTotalQueue()));






    }


    public void LoadGroupListInChoiceBox()
    {
        String sql = "select Listname from ListGroup";
        int i=0;

        ObservableList<String> data = FXCollections.observableArrayList();

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                i++;

                data.add( rs.getString("Listname"));

            };

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        choicebox01.setItems(data);
        choicebox01.setTooltip(new Tooltip("Select List"));
        choicebox01.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov, Number oldValue, Number newValue) {
                // label.setText(greetings[newValue.intValue()]);
                //String output = ov.


                LoadListViewGroupList(newValue.intValue());

            }
        });
    }

    public void LoadListViewGroupList(int val) {

        int val1 = val;
        val1=val1+1;


        String sql = "select listgroup.id, listgroup.listname, listgroupdetail.fbid, fbgroup.name, fbgroup.url" +
                " from listgroup inner join listgroupdetail " +
                " on listgroupdetail.listgroupid = listgroup.id\n" +
                " inner join fbgroup \n" +
                " on listgroupdetail.fbid = fbgroup.fbid \n" +
                " where listgroup.id = ?";

        int i=0;

        ObservableList<String> data = FXCollections.observableArrayList();

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql))
        {

            pstmt.setInt(1, val1);
           // System.out.println(sql);
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                i++;

                data.add( rs.getString("name"));

            };

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        listView02.setItems(data);
       // listView02.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        LoadListViewGroup("Select name from fbgroup where isactive = 1 and fbid not in (select fbid from listgroupdetail where listgroupid="+val1+")");
    }


    public void LoadListViewGroup(String mSql) {

        String sql = "select name from fbgroup where isactive=1";
        if (mSql != "") sql = mSql;

        int i=0;

        ObservableList<String> data = FXCollections.observableArrayList();

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                i++;

                data.add( rs.getString("name"));

            };

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        listView01.setItems(data);
        listView01.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


    }

    public int GetGroupListId(int selindex)
    {
        return selindex+1;


    }

    public void SaveCampaign(ActionEvent event)
    {

        int campaignId;
        String msg;
        String cName;

        cName = textCampaign.getText();
        msg=txtMessage.getText();
        campaignId=Integer.parseInt(lblCampaignId.getText());


        int listGroupId = GetGroupListId(choiceboxtarget02.getSelectionModel().getSelectedIndex());

        // debug
        System.out.println("Campaign id =" + campaignId);
        System.out.println("Text=" + msg);
        System.out.println("list group id = "+ listGroupId);

        Campaign c = new Campaign();
        c.setid(1);
        c.setname(cName);
        c.setQueueCreated(0);
        c.settextMessage(msg);
        c.setlistGroupId(listGroupId);
        c.save();

//
    }

    public void GenerateQueue()
    {


        //get current id
        int campaignid = Integer.valueOf(lblCampaignId.getText());
        Campaign c = new Campaign();
        c.load(campaignid);
        c.CreateMessageQueue();
        int total =c.GetTotalQueue();
        lblQueueCreated.setText(String.valueOf(total));
    }

    public void ClearAllQueue()
    {
        // check campaign id
        //int campaignid = Integer.valueOf(lblCampaignId.getText());
        int campaignid =1;
        Campaign c = new Campaign();
        c.load(campaignid);
        c.ClearAllQueue();
        //lblQueueCreated.setText("0");

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


    public void LoadAccount()
    {
        String login;
        String password;

        fbAccount fbac = new fbAccount();
        fbac.selectFirstId();


        login= fbac.getLogin();
        password = fbac.getPassword();


        txtLogin.setText(login);
        txtPassword.setText(password);

    }

    public void LoadAccountDetail(ActionEvent event)
    {
        LoadAccount();
    }


    // handle adding record from listview all to listview grouplist
    public void AddSelectedRecordToListGroup(ActionEvent event)
    {
        //1. get selected item in listview
        //2. get active listgroup
        //3. update database record for particular group list.
        //4. refresh record

        int item;
        String itemText;
        String listgrouptext ;

        item = listView01.getSelectionModel().getSelectedIndex();
        itemText=listView01.getSelectionModel().getSelectedItem().toString();
        listgrouptext=choicebox01.getSelectionModel().getSelectedItem().toString();
        int cbosel = choicebox01.getSelectionModel().getSelectedIndex();



        System.out.println("found item " + itemText + " selected");
        System.out.println("found item " + listgrouptext + " selected");

        // get id for the group name
        fbgroup g = new fbgroup();
        g.GetFBGroupbyname(itemText);

        fbListGroup l = new fbListGroup();
        l.GetFbListGroupByName(listgrouptext);

        String fbid = g.getFbId();
        int listId = l.getId();

        String sql = "insert into listgroupdetail (fbid, isactive,listgroupid) values (?,1,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fbid);
            pstmt.setInt(2, listId);
            pstmt.executeUpdate();
            //System.out.println("Data inserted");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        //System.out.println("choicebox selected = "+cbosel);
        LoadListViewGroupList(cbosel);
        cbosel=cbosel+1;
        LoadListViewGroup("Select name from fbgroup where isactive = 1 and fbid not in (select fbid from listgroupdetail where listgroupid="+cbosel+")");




    }

    // handle removing selectedrecord from listview grouplist
    public void RemoveSelectedRecordToListGroup(ActionEvent event)
    {
        int item;
        String itemText;
        String listgrouptext ;

        item = listView02.getSelectionModel().getSelectedIndex();
        itemText=listView02.getSelectionModel().getSelectedItem().toString();
        listgrouptext=choicebox01.getSelectionModel().getSelectedItem().toString();
        int cbosel = choicebox01.getSelectionModel().getSelectedIndex();



        //System.out.println("found item " + itemText + " selected");
        //System.out.println("found item " + listgrouptext + " selected");

        // get id for the group name
        fbgroup g = new fbgroup();
        g.GetFBGroupbyname(itemText);

        fbListGroup l = new fbListGroup();
        l.GetFbListGroupByName(listgrouptext);

        String fbid = g.getFbId();
        int listId = l.getId();

        String sql = "delete from listgroupdetail where fbid=?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fbid);
            pstmt.executeUpdate();
            //System.out.println("Data inserted");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        //System.out.println("choicebox selected = "+cbosel);
        LoadListViewGroupList(cbosel);
        cbosel=cbosel+1;
        LoadListViewGroup("Select name from fbgroup where isactive = 1 and fbid not in (select fbid from listgroupdetail where listgroupid="+cbosel+")");



    }


    public void generateRandom(ActionEvent event){
        Random rand = new Random();
        int myrand = rand.nextInt(50)+1;
    }

    public void Login(ActionEvent e)
    {
        prmLogin = txtLogin.getText();
        prmPassword = txtPassword.getText();

        //System.out.println(prmLogin + "," + prmPassword);

        //Call
        fbAccount fbac = new fbAccount();
        fbac.update(prmLogin, prmPassword);
        System.out.println("Login data updated");

    }


    public void logintofb(String login, String pass)
    {
        String url = "http://en-gb.facebook.com";

        //String gUrl = "https://www.facebook.com/groups/freedslrworkshop/";


        String geckoPath = "c:\\geckodriver\\";


        System.setProperty("webdriver.gecko.driver",geckoPath + "geckodriver.exe");

        d = new FirefoxDriver();
        d.get(url);

        d.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        d.findElement(By.id("email")).sendKeys(login);
        d.findElement(By.id("pass")).sendKeys(pass);
        d.findElement(By.xpath("//input[@value='Log In']")).click();
        d.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);


    }

    public boolean PostToGroup(String groupurl, PostContent pc) throws InterruptedException
    {
        Actions actions = new Actions(d);

        try {
            d.get(groupurl);
        }
        catch (UnhandledAlertException e)
        {

            actions.sendKeys(Keys.F5);
            actions.sendKeys(Keys.ENTER);
            d.get(groupurl);
        }

        String postBtnIdentifier = ".//*[@data-testid='react-composer-post-button']";
        boolean postingBox =false;
        boolean finalPost = false;

        try {

            //search for posting box
            // begin by searchin keyword Write Post. If non to be found, search for Start DIscussion

            try {
                d.findElement(By.xpath("//span[text()='Write Post']")).click();
                d.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
                postingBox = true;
            } catch (NoSuchElementException e){
                // TODO: handle finally clause
                System.out.print("Write Post failed to be found");
            }

            if (!postingBox){
                try {
                    d.findElement(By.xpath("//span[text()='Start Discussion']")).click();
                    d.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
                    postingBox = true;
                } catch (NoSuchElementException e){
                    // TODO: handle finally clause
                    System.out.print("Start discussion failed to be found");
                }
            }


            if (postingBox)
            {


                //NaturalTyping(actions, pc.gettextmessage());
                actions.sendKeys(pc.gettextmessage()).perform();

                d.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

                actions.sendKeys(" ").perform();
                // wait for a while
                d.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                Thread.sleep(15000);

                //check if photo available
                if(pc.getmediaurl1()!=null) {
                    WebElement fileInput = d.findElement(By.name("composer_photo[]"));
                    fileInput.sendKeys(pc.getmediaurl1());
                    d.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                }


                if(pc.getmediaurl2()!=null) {
                    WebElement fileInput2 = d.findElement(By.name("composer_photo"));
                    fileInput2.sendKeys(pc.getmediaurl2());
                    d.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
                }


                if(pc.getmediaurl3()!=null) {
                    WebElement fileInput3 = d.findElement(By.name("composer_photo"));
                    fileInput3.sendKeys(pc.getmediaurl3());

                    d.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                }


                WebElement postBtn = d.findElement(By.xpath(postBtnIdentifier));
                postBtn.sendKeys(Keys.ENTER);


                d.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
                finalPost= true;
            } // end postingBox

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            finalPost= false;
        }

        return finalPost;
    }

    public static void NaturalTyping(Actions a, String txt) throws InterruptedException
    {
        Random ran = new Random();
        int x;

        x= ran.nextInt(3) * 2;

        char[] stringToCharArray = txt.toCharArray();
        for (char output : stringToCharArray) {
            Thread.sleep(x);
            a.sendKeys(String.valueOf(output)).perform();
        }

    }
    public void CreateSendingList()
    {
        CreateMesssageQueue();

    }

    public void CallSend() throws InterruptedException
    {
        anime.pause();
        //System.out.println("Method -> CallSend");
        // sql = select * from FBGroup where sent=0 limit 1
        MessageQueue q = new MessageQueue();
        PostContent pc = new PostContent();

        boolean foundrec = false;
        foundrec = q.GetMesssageInQueue();
        String txt;
        boolean saved=false;

        if (foundrec)
        {
            System.out.println("Processing now:\nID="+q.getId() + "\nURL=" + q.getFbid());

            // continue

            // updating fb to set sent status = 1
            //fb.setSent(1);
            int id = q.getId();
            txt = q.getTextMessage();
            pc = q.getPostcontent();


            String url;
            url ="https://www.facebook.com/groups/"+q.getFbid();

            if (PostToGroup(url, pc))
            {
                //fb.Update(id,fb.getName(), fb.getUrl(),1);
                q.setSent(1);
                saved=q.Save();
            }
            else
            {
                System.out.println("Fail to Post To Group");
            }
            anime.play();
        }
        else
        {
            System.out.println("No more record found");

            anime.stop();
            btnRunSender.setText("Run Sender");
        }

    }

    public void SendNow()
    {
        try {
            CallSend();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void CreateMesssageQueue()
    {
        MessageQueue mq = new MessageQueue();
        mq.CreateMQ();
        mq.UpdateMQContent();

    }


    public void RunSender(ActionEvent event)
    {

        System.out.println("Method=RunSender");
        System.out.println("btnRunSender.text="+btnRunSender.getText());
        //Timeline anim = null;
        //System.out.println("Sending is in progress");
        if (btnRunSender.getText().equals("Run Sender"))
        {

            // check if queue available
            //MessageQueue m = new MessageQueue();
            //if (m.GetCurrentTotalQueue()==0)
            //{
                // populate queue

            //}

            //System.out.println("If 1");
            btnRunSender.setText("Stop sending");

            // need error checkin
            logintofb(txtLogin.getText(), txtPassword.getText());

            anime = new Timeline(new KeyFrame(Duration.seconds(300), new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    try {
                        CallSend();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }));
            anime.setCycleCount(Timeline.INDEFINITE);
            anime.play();
        }
        else
        {
            btnRunSender.setText("Run Sender");
            System.out.println("Process is stopped");
            anime.stop();

        }


    }

    public void DeleteAllRecord(ActionEvent event)
    {
        fbgroup fb = new fbgroup();
        fb.DeleteAllRecord();

    }



    public void UpdateGroup(ActionEvent event) throws InterruptedException{

        boolean ok=true;

        if (txtLogin.getText().trim().isEmpty())
        {
            System.out.println("Login empty");
            ok=false;
        }
        if (txtPassword.getText().trim().isEmpty())
        {
            System.out.println("Password empty");
            ok=false;
        }

        if (ok)
        {
            prmLogin = txtLogin.getText().trim();
            prmPassword = txtPassword.getText().trim();

            System.out.println("Login="+prmLogin + "," + "Password=" + prmPassword);


            fbpost post = new fbpost();

            try {
                post.UpdateGroupRecord(prmLogin,prmPassword);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else
        {

            System.out.println("Error. Aborting");

        }

        LoadListViewGroup("");


    }

}
