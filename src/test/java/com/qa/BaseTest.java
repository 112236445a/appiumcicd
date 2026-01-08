package com.qa;

import com.google.common.collect.ImmutableMap;
import com.qa.utils.TestUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.*;
import java.net.URL;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;



//Create a super class
public class BaseTest {
    //private static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    //Use ThreadLocal to define the object to support for parallel execution
    protected static ThreadLocal <AppiumDriver> driver = new ThreadLocal<AppiumDriver>();//add static to make the passing constructor belongs to the class
    //protected static IOSDriver iosDriver;
    protected static ThreadLocal <Properties> props = new ThreadLocal<Properties>();
    protected static ThreadLocal <String> platform = new ThreadLocal<String>();
    protected static ThreadLocal <String> dateTime = new ThreadLocal<String>();
    protected static ThreadLocal <String> deviceName =new ThreadLocal<String>();
    static Logger log = LogManager.getLogger(BaseTest.class.getName());
    private static AppiumDriverLocalService server;
    TestUtils utils = new TestUtils();

    //Need to use setter getter methods
    public AppiumDriver getDriver(){
        return driver.get();//driver.get(): returns the value in the current threads to ensure the thread safety
    }
    public void setDriver(AppiumDriver driver2){
        driver.set(driver2);//setter methods for setting the value of the object for this thread
    }

    public Properties getProperty(){
        return props.get();
    }
    public void setProperty(Properties props2){
        props.set(props2);
    }

    public String getPlatform(){
        return platform.get();
    }
    public void setPlatform(String platform2){
        platform.set(platform2);
    }

    public String getDeviceName(){
        return deviceName.get();
    }

    public void setDeviceName(String deviceName2){
        deviceName.set(deviceName2);
    }

    public String getDateTime(){
        return dateTime.get();//dateTime.get(): returns the value in the current threads to ensure the thread safety
    }
    public void setDateTime(String dateTime2){
        dateTime.set(dateTime2);
    }


    @BeforeMethod
    public void beforeMethod(){
        ((CanRecordScreen) getDriver()).startRecordingScreen(); //Class and method to record video
    }

    @AfterMethod
    public void afterMethod(ITestResult result){
        String media = ((CanRecordScreen) getDriver()).stopRecordingScreen();//.stopRecordingScreen(): return a base 64 encoded string

        if (result.getStatus()==2){//For the Recording when the TC fail. Can check inside the class for status code
            Map<String, String> params = result.getTestContext().getCurrentXmlTest().getAllParameters();//getAllParameters() already returns a Map
            String dir = "videos" + File.separator + params.get("platformName") + File.separator + params.get("deviceName")
                    + File.separator + getDateTime() + File.separator + result.getTestClass().getRealClass().getSimpleName();//result.getTestClass().getRealClass().getSimpleName() get the Class name

            File videoDir = new File(dir);
            if (!videoDir.exists()){
                videoDir.mkdirs(); //if the path doesn't exists, create a path for that. If exists, will override it
            }

            try {
                FileOutputStream stream =new FileOutputStream(videoDir + File.separator + result.getName()+".mp4");//result.getName() get the Test method name
                stream.write(Base64.getDecoder().decode(media)); //Base64 come from Apache Commons Codec dependency
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @BeforeSuite //For the single appium instance, use BeforeSuite annotation. For multi thread, use BeforeMethod
    public void beforeSuite(){
        server = getAppiumServerDefault();
        if (!server.isRunning()){
            server.start(); //Start the appium server
            server.clearOutPutStreams();//Doesn't output the Appium server log
            utils.log("Appium Server Started");
            System.out.println("Appium Server Started");
        }else {
            utils.log("Appium Server Already Started");
            System.out.println("Appium Server Already Started");
        }
    }

    @AfterSuite
    public void afterSuite(){
        server.stop();
        utils.log("Appium Server Stopped");
        System.out.println("Appium Server Stopped");
    }

    public AppiumDriverLocalService getAppiumServerDefault(){
        return AppiumDriverLocalService.buildDefaultService();
    }

    @Parameters({"emulator", "platformName", "udid","deviceName", "wdaLocalPort", "systemPort", "chromeDriverPort"})
    @BeforeTest //Annotation for execute in test level. It will be executed before any of the test classes is executed
    public void beforeTest(@Optional("iOSOnly")String emulator, String platformName, String udid, String deviceName, @Optional("androidOnly")String wdaLocalPort, @Optional("iOSOnly")String systemPort, @Optional("iOSOnly")String chromeDriverPort) throws Exception {

        //Convert the class level variables to local variables inside methods
        setDateTime(utils.dateTime());
        setPlatform(platformName); // Pass the local variable platformName to global variable by setter method
        setDeviceName(deviceName);
        InputStream inputStream;
        Properties props;
        AppiumDriver driver;
        URL url;

        String strFile = "log" + File.separator + platformName + "_" + deviceName;
        File logFile = new File(strFile);
        if(!logFile.exists()){
            logFile.mkdirs();
        }
        ThreadContext.put("ROUTINGKEY",strFile); //ThreadContext: Hashmap. The value of ROUTINGKEY will be different due to different thread (strFile name)
        //"ROUTINGKEY" can be define by ourselves but must be align with log4j2.xml one
        try {
            props = new Properties();
            String proFileName = "config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(proFileName);
            props.load(inputStream);
            setProperty(props);

            //Initialize the diver by once which will be available for all the Test classes
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("appium:platformName",platformName); //Create the parameter inside TestNG XML and pass to here
            caps.setCapability("appium:newCommandTimeout",1000);

            url = new URL(props.getProperty("appiumURL") + "4723");//For running on same appium server instances

            switch (platformName.toLowerCase()){
                case "android":
                    //url = new URL(props.getProperty("appiumURL") + "4723");//For running on different appium server instances
                    caps.setCapability("appium:automationName", props.getProperty("androidAutomationName"));//****
                    //caps.setCapability("appium:deviceName", "Pixel_5");
                    caps.setCapability("appium:appPackage", props.getProperty("androidAppPackage"));//***
                    caps.setCapability("appium:appActivity", props.getProperty("androidAppActivity"));//***
                    if (emulator.equalsIgnoreCase("true")){
                        caps.setCapability("appium:avd", deviceName); //Create the parameter inside TestNG XML and pass to here
                    }else {
                        caps.setCapability("appium:udid", udid); //Create the parameter inside TestNG XML and pass to here
                    }
                    caps.setCapability("appium:avdLaunchTimeout", 180);
                    //String androidAppUrl = System.getProperty("user.dir") + File.separator+ "src"+ File.separator+ "main"+ File.separator + "resources" + File.separator + "app" + File.separator + "Android.SauceLabs.Mobile.Sample.app.2.7.1.apk";
                    String androridAppURL = getClass().getResource(props.getProperty("androidAppLocation")).getFile();//Have to use .getFile() way otherwise return null
                    caps.setCapability("appium:systemPort", systemPort);
                    caps.setCapability("appium:chromedriverPort", chromeDriverPort);
                    utils.log().info(androridAppURL);
                    caps.setCapability("appium:app", androridAppURL);
                    driver = new AndroidDriver(url,caps);
                    //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                    // The automationName, appPackage, appActivity and URL are global configuration parameters.
                    // Need to store them in a config.properties file
                    utils.log().info("Platform is:" +platformName);
                    break;
                case "ios":
                    //url = new URL(props.getProperty("appiumURL") + "4724");//For running on different appium server instances
                    caps.setCapability("appium:automationName", props.getProperty("iOSAutomationName"));//****
                    caps.setCapability("appium:udid", udid); //Create the parameter inside TestNG XML and pass to here
                    caps.setCapability("appium:bundleId", props.getProperty("iOSBundleID"));//***
                    if (emulator.equalsIgnoreCase("true")){
                        caps.setCapability("appium:avd", deviceName);//Create the parameter inside TestNG XML and pass to here

                    }else {
                        caps.setCapability("appium:deviceName", deviceName);
                    }
                    caps.setCapability("appium:avdLaunchTimeout", 180);
                    caps.setCapability("appium:simpleIsVisibleCheck", true);
                    //String iOSAppUrl = System.getProperty("user.dir") + File.separator+ "src"+ File.separator+ "test"+ File.separator + "java" + getClass().getClassLoader().getResource("androidAppLocation");
                    String iOSappURL = getClass().getResource(props.getProperty("iOSAppLocation")).getFile();//Have to use .getFile() way otherwise return null
                    String iOSappURLReal = getClass().getResource(props.getProperty("iOSAppLocationReal")).getFile();
                    /*if (emulator.equals("true")){
                        caps.setCapability("appium:app", iOSappURL);
                    }else {
                        caps.setCapability("appium:app", iOSappURLReal);
                    }*/

                    caps.setCapability("appium:wdaLocalPort", wdaLocalPort);
                    utils.log().info("Platform is:" +platformName);
                    driver = new IOSDriver(url,caps);
                    break;
                default:
                    throw new Exception("Invalid platform");
            }
            setDriver(driver);

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }


    public void waitForVisibility(WebElement e){//Create a method for visible the target element purpose instead of implicitlyWait method
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(TestUtils.WAIT));//Create class to hide it
        wait.until(ExpectedConditions.visibilityOf(e));
    }

    public void click(WebElement e){//Create a method for clcik the target element purpose instead of hardcode
        utils.log().info("Clicking the element");
        waitForVisibility(e); //To check the visibility of the element first *******
        e.click();
    }

    public void sendKeys(WebElement e, String txt){//Create a method for sending keys the target element purpose instead of hardcode
        utils.log().info("Sending the keys:" + txt);
        waitForVisibility(e);
        e.sendKeys(txt);
    }

    public String getAttribute(WebElement e, String attribute){//Create a method for getting attribute the target element purpose instead of hardcode
        utils.log().info("Getting the Attribute");
        waitForVisibility(e);
        return e.getAttribute(attribute);
    }

    public String getText(WebElement e){
        switch (getPlatform().toLowerCase()){
            case "android":
                utils.log().info("Getting the Text:" + e.getText());
                return getAttribute(e, "text");
            case "ios": //Since iOS only have label instead of text, have to create a switch for getting text
                utils.log().info("Getting the Text:" + e.getText());
                return getAttribute(e,"label");
        }
        return null;
    }

    public void clearField(WebElement e){
        utils.log().info("Clearing the Field...");
        waitForVisibility(e);
        e.clear();
    }

    public void closeApp(){
        switch (getPlatform().toLowerCase()){
            case "android":
                utils.log().info("Terminate App");
                ((AndroidDriver) getDriver()).terminateApp(getProperty().getProperty("androidAppPackage"));
                break;

            case "ios":
                utils.log().info("Terminate App");
                ((IOSDriver) getDriver()).terminateApp(getProperty().getProperty("iOSBundleID"));
                break;
        }
    }

    public void launchApp(){
        switch (getPlatform().toLowerCase()){
            case "android":
                utils.log().info("Launching App");
                ((AndroidDriver) getDriver()).activateApp(getProperty().getProperty("androidAppPackage"));
                break;
            case "ios":
                utils.log().info("Launching App");
                ((IOSDriver) getDriver()).activateApp(getProperty().getProperty("iOSBundleID"));
                break;
        }
    }

    public void scrolling(WebElement element){//Scrolling into View method*******
        switch (getPlatform().toLowerCase()){
            case "android":
                utils.log().info("Performing Scrolling");
                getDriver().findElement(AppiumBy.androidUIAutomator("new UiScrollable(new UiSelector()" + ".scrollable(true)).scrollIntoView(" + "new UiSelector().description(\"test-Price\"));"
                ));
                break;
            case "ios":
                utils.log().info("Performing Scrolling");
                getDriver().executeScript("mobile:scrollToElement", ImmutableMap.of(
                        //"predicateString", "label == 'ADD TO CART'",
                        "elementId", ((RemoteWebElement) element).getId()
                        //"toVisible", "ad"
                ));
                break;
        }
        //return driver.findElement(AppiumBy.androidUIAutomator("new UiScrollable(new UiSelector()" + ".description(\"test-Inventory item page\")).scrollIntoView(" + "new UiSelector().description(\"test-Price\"));"


    }


    @AfterTest
    public void afterTest(){
        utils.log().info("Driver quit");
        getDriver().quit();
    }
}
