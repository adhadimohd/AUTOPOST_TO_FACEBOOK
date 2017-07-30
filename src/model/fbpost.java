package model;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class fbpost {
    private Connection Db;
    private String connstr = "jdbc:sqlite:autopost.db";

    public Connection ConnectDB()
    {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(this.connstr);
            return c;
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return null;
        }

    }

    public void UpdateGroupRecord(String login, String pass) throws InterruptedException
    {

        String geckoPath = "c:\\geckodriver\\";
        String name = "";
        String url="";


        System.setProperty("webdriver.firefox.marionette",geckoPath + "geckodriver.exe");
        WebDriver driver = new FirefoxDriver();
        driver.get("http://en-gb.facebook.com");
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.findElement(By.id("email")).sendKeys(login);
        driver.findElement(By.id("pass")).sendKeys(pass);
        driver.findElement(By.xpath("//input[@value='Log In']")).click();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

        driver.get("https://www.facebook.com/groups/?category=membership");
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

        List<WebElement> list = driver.findElements(By.xpath(".//a[contains(@href,'/groups/') and contains(@data-hovercard,'/ajax/')]"));
        int oldList = list.size();
        int newList = 0;
        boolean t = false;

        int counter = 0;
        JavascriptExecutor jse = (JavascriptExecutor) driver;

        while (oldList != newList) {
            counter++;
            oldList = list.size();
            //System.out.println(counter);
            Actions actions = new Actions(driver);
            actions.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();

            Thread.sleep(2000);


            while (!t) {
                t = jse.executeScript("return document.readyState").toString().equals("complete");
            }

            list = driver.findElements(By.xpath(".//a[contains(@href,'/groups/') and contains(@data-hovercard,'/ajax/')]"));
            newList = list.size();

        }

        // open database/table
        // for each element found, check existing url, if not update db.



        fbgroup fb = new fbgroup();
        fb.initializeTable(this.connstr);
        int status = 0;
        String fbid;

        for (WebElement e : list) {
            status=0;
            name = e.getAttribute("innerHTML");
            url = e.getAttribute("href");
            fbid = url;
            fbid= fbid.replace("https://www.facebook.com/groups/","");
            fbid = fbid.replace("/?ref=group_browse_new","");


            //System.out.println(e.getAttribute("innerHTML"));
            //System.out.println(e.getAttribute("href"));

            status = fb.Insert(name,url,fbid);

            if (status == 0)
            {
                System.out.println("error inserting data for " + name);
            } else
            {
                System.out.println("Successfully inserting data for " + name);
            }
        }
        System.out.println("total : " + list.size());

        try {
            fb.RefreshGroup();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }


        driver.close();
    }


    public void PostMsgToFB(String login , String pass) throws InterruptedException{

        String msg = "http://adhadi.com/shop/dvd-kursus-asas-photoshop-cs5/";
        String url = "http://en-gb.facebook.com";
        String postBtnIdentifier = ".//*[@data-testid='react-composer-post-button']";
        String gUrl = "https://www.facebook.com/groups/freedslrworkshop/";



        String geckoPath = "c:\\geckodriver\\";


        System.setProperty("webdriver.firefox.marionette",geckoPath + "geckodriver.exe");

        WebDriver d = new FirefoxDriver();
        d.get(url);

        d.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        d.findElement(By.id("email")).sendKeys(login);
        d.findElement(By.id("pass")).sendKeys(pass);
        d.findElement(By.xpath("//input[@value='Log In']")).click();
        d.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

        d.get(gUrl);

        // Find Textbox container in group and initialize
        d.findElement(By.xpath("//span[text()='Start Discussion']")).click();
        //wait for second
        Thread.sleep(1000);

        Actions actions = new Actions(d);

        // send the text
        actions.sendKeys(msg).perform();
        Thread.sleep(1000);
        actions.sendKeys(" ").perform();
        // wait for a while
        Thread.sleep(3000);

        WebElement postBtn = d.findElement(By.xpath(postBtnIdentifier));
        postBtn.sendKeys(Keys.ENTER);
        d.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);


    }



}
