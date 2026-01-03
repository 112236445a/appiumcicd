package qa.mobile;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class test1 {
     AppiumDriver driver;
    @Test
    public void invalidUserName(){
        By userNameField = AppiumBy.accessibilityId("test-Username");
        By passWordField = AppiumBy.accessibilityId("test-Password");
        By loginButton = AppiumBy.accessibilityId("test-LOGIN");
        By errTxt = AppiumBy.androidUIAutomator("new UiSelector().text(\"Username and password do not match any user in this service.\")");

        driver.findElement(userNameField).sendKeys("Invalid");
        driver.findElement(passWordField).sendKeys("fake");
        driver.findElement(loginButton).click();

        String actualTxt = driver.findElement(errTxt).getAttribute("text");
        System.out.println("Actual Error Text:" + actualTxt);
        String expectedTxt = "Username and password do not match any user in this service.";

        Assert.assertEquals(actualTxt,expectedTxt); //Use Assertion to verify. *******
    }

    @Test
    public void invalidPassword(){
        By userNameField = AppiumBy.accessibilityId("test-Username");
        By passWordField = AppiumBy.accessibilityId("test-Password");
        By loginButton = AppiumBy.accessibilityId("test-LOGIN");
        By errTxt = AppiumBy.androidUIAutomator("new UiSelector().text(\"Username and password do not match any user in this service.\")");

        driver.findElement(userNameField).sendKeys("standard_user");
        driver.findElement(passWordField).sendKeys("fake");
        driver.findElement(loginButton).click();

        String actualTxt = driver.findElement(errTxt).getAttribute("text");
        System.out.println("Actual Error Text:" + actualTxt);
        String expectedTxt = "Username and password do not match any user in this service.";

        Assert.assertEquals(actualTxt,expectedTxt);
    }

    @Test
    public void validLogin(){
        By userNameField = AppiumBy.accessibilityId("test-Username");
        By passWordField = AppiumBy.accessibilityId("test-Password");
        By loginButton = AppiumBy.accessibilityId("test-LOGIN");
        By productTxt = AppiumBy.androidUIAutomator("new UiSelector().text(\"PRODUCTS\")");

        driver.findElement(userNameField).sendKeys("standard_user");
        driver.findElement(passWordField).sendKeys("secret_sauce");
        driver.findElement(loginButton).click();

        String actualTxt = driver.findElement(productTxt).getAttribute("text");
        System.out.println("Actual Product Text:" + actualTxt);
        String expectedTxt = "PRODUCTS";

        Assert.assertEquals(actualTxt,expectedTxt);
    }

    @BeforeClass
    public void beforeClass() throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("appium:platformName","Android");
        caps.setCapability("appium:newCommandTimeout",300);
        URL url = new URL("http://0.0.0.0:4723");

        caps.setCapability("appium:automationName", "uiautomator2");
        caps.setCapability("appium:deviceName", "Pixel_5");
        caps.setCapability("appium:udid", "emulator-5544");
        String androidAppUrl = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "ApiDemos-debug.apk";
        caps.setCapability("appium:appPackage", "com.swaglabsmobileapp");
        //caps.setCapability("appium:app", androidAppUrl); //For install the app
        caps.setCapability("appium:appActivity", "com.swaglabsmobileapp.MainActivity");
        //caps.setCapability("appium:appPackage", "com.google.android.apps.maps");
        //caps.setCapability("appium:appActivity", "com.google.android.maps.MapsActivity");
        caps.setCapability("appium:avd", "Pixel_5");
        caps.setCapability("appium:avdLaunchTimeout", 180);
        //caps.setCapability("appium:unlockType", "pin"); //unlock the pin if the pin has been set
        //caps.setCapability("appium:unlockType", "pattern"); //unlock the pattern if the pattern has been set
        //caps.setCapability("appium:unlockKey","1235789"); //unlock the pin password
        //caps.setCapability("appium:chromedriverExecutable","'/Users/ming/Downloads/chrome-mac-arm64/Google Chrome for Testing.app'"); //For using this specific chrome driver instead of Appium that one
        //caps.setCapability("browserName", "Chrome"); //For launching the browser. For Browser testing
        driver = new AndroidDriver(url,caps);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterClass
    public void afterClass(){
        driver.quit();
    }
}
