package com.qa;

import com.qa.pages.settingPage;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class MenuPage extends BaseTest {
    public MenuPage(){
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.ImageView\").instance(1)")
    @iOSXCUITFindBy(accessibility = "test-Menu")
    private WebElement settingButton;

    public settingPage pressSettingButton(){
        click(settingButton);
        return new settingPage();
    }
}
