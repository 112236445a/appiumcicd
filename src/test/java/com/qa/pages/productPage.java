package com.qa.pages;

import com.qa.MenuPage;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class productPage extends MenuPage {//Extends MenuPage for whichever page use MenuPage as common
    public productPage(){
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this); //Have to create the constructor for initiate the driver here and pass to BaseTest class
    }

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"PRODUCTS\")")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"test-Toggle\"]/parent::*[1]/preceding-sibling::*[1]")//need to use customer xpath to retrive the text
    private WebElement productTxt;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Sauce Labs Backpack\")")
    @iOSXCUITFindBy(iOSNsPredicate = "name == \"test-Item title\" AND label == \"Sauce Labs Backpack\"")
    private WebElement productName;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"$29.99\")")
    @iOSXCUITFindBy(iOSNsPredicate = "name == \"test-Price\" AND label == \"$29.99\"")
    private WebElement productPrice;

    public String productTxt(){
        return getText(productTxt);
    }

    public String productName(){
        String productNameTxt = getText(productName);
        System.out.println("Product Name:" + productNameTxt);
        return productNameTxt;
    }

    public String productPri(){
        String productPriTxt = getText(productPrice);
        System.out.println("Product Price:" + productPriTxt);
        return productPriTxt;
    }

    public productDetailsPage productDetail(){
        click(productName);
        return new productDetailsPage();
    }
}
