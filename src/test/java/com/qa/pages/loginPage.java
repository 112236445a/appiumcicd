package com.qa.pages;

import com.qa.BaseTest;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class loginPage extends BaseTest {
    public loginPage(){
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);//For initializing the UI elements for page factory
    }

    @AndroidFindBy(accessibility = "test-Username")
    @iOSXCUITFindBy(accessibility = "test-Username")
    private WebElement userNameField;

    @AndroidFindBy(accessibility = "test-Password")
    @iOSXCUITFindBy(accessibility = "test-Password")
    private WebElement passWordField;

    @AndroidFindBy(accessibility = "test-LOGIN")
    @iOSXCUITFindBy(accessibility = "test-LOGIN")
    private WebElement loginButton;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Username and password do not match any user in this service.\")")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"test-Error message\"]/child::XCUIElementTypeStaticText")//For static text, better to create xpath to find the element
    private WebElement errTxt;

    public loginPage enterUserName(String username) {
        clearField(userNameField);
        sendKeys(userNameField, username);
        return this;
        //When on a page and performing some action on the same, have to return the object of particular page******
        //If an action bring to another page, do not need to use return
    }
    public loginPage enterPassword(String password) {
        clearField(passWordField);//have to clear the field for iOS platform. some handling might affect other platform***
        sendKeys(passWordField, password);
        return this;
    }
    public productPage loginAction(){//Use productPage since it direct to productPage object
        click(loginButton);
        System.out.println("Press login Button");
        return new productPage();
    }
    public productPage validLogin(String userID, String password){
        enterUserName(userID);
        System.out.println("Login with ID: " +userID);
        enterPassword(password);
        System.out.println("Login with Password: " +password);
        return loginAction();
    }

    public String getErrorTxt(){
        return getText(errTxt);
    }
}
