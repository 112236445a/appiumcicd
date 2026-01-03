package com.qa.pages;

import com.qa.BaseTest;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class settingPage extends BaseTest {
    public settingPage(){
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()),this);
    }

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"LOGOUT\")")
    @iOSXCUITFindBy(accessibility = "test-LOGOUT")
    private WebElement logoutBtn;


    public loginPage logout(){
        click(logoutBtn);
        return new loginPage();
    }
}
