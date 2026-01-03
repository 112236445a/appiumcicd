package com.qa.pages;

import com.qa.BaseTest;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.PageFactory;

public class productDetailsPage extends BaseTest {
    public productDetailsPage(){
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()),this);
    }

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Sauce Labs Backpack\")")
    @iOSXCUITFindBy(accessibility = "Sauce Labs Backpack")
    private WebElement productName;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection.\")")
    @iOSXCUITFindBy(accessibility = "carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection.")
    private WebElement productDescription;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"BACK TO PRODUCTS\")")
    @iOSXCUITFindBy(accessibility = "test-BACK TO PRODUCTS")
    private WebElement backToProduct;

    @iOSXCUITFindBy(className = "XCUIElementTypeScrollView")
    private WebElement productDetailPageScrollingElement;

    @AndroidFindBy(accessibility = "test-Price" )
    @iOSXCUITFindBy(accessibility = "test-Price")
    private WebElement productPrice;

    @iOSXCUITFindBy(accessibility = "test-ADD TO CART")
    @AndroidFindBy(accessibility = "test-Price" )
    private WebElement addToCart;


    public String getProductName(){
        return getText(productName);
    }

    public String getProductDescription(){
        return getText(productDescription);
    }

    public productPage backToProductPage(){
        click(backToProduct);
        return new productPage();
    }

    public void ScrollingToAddToCart(){
        scrolling(addToCart);
        }

    public String getProductPrice(){//Combine scrolling and getTxt method
       return getText(productPrice);
    }


    public boolean addToCartDisplayed(){
        return addToCart.isDisplayed();
    }

}
