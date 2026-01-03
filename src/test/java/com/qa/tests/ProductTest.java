package com.qa.tests;

import com.qa.BaseTest;
import com.qa.pages.loginPage;
import com.qa.pages.productDetailsPage;
import com.qa.pages.productPage;
import com.qa.pages.settingPage;
import com.qa.ui.DeepLink;
import com.qa.utils.TestUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.InputStream;
import java.lang.reflect.Method;

public class ProductTest extends BaseTest { //Have to extends super class
    loginPage loginPage;
    productPage productPage;
    productDetailsPage productDetailsPage;
    settingPage settingPage;
    JSONObject loginUsers;
    TestUtils utils = new TestUtils();

    @BeforeClass
    private void beforeClass() throws Exception {
        //Convert the class level variables to local variables inside methods
        InputStream details = null; //Create object from InputStream for reading json data
        try{//Better to use Try catch block and finally to close the InputStream
            String dataFileName = "Data/loginUser.json";
            details = getClass().getClassLoader().getResourceAsStream(dataFileName);
            JSONTokener tokener = new JSONTokener(details);
            loginUsers = new JSONObject(tokener);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            if (details != null){
                details.close();
            }
        }
    }

    @BeforeMethod
    public void beforeMethod(Method m){
        loginPage = new loginPage();
        utils.log().info("\n" +"****** Starting Test:"+ m.getName() + "******" + "\n");
        closeApp(); //need to close and launch the app otherwise it will stuck in ProductPage since the validLogin need to execute in loginPage
        launchApp();
    }

    @Test
    public void validProductOnProductPage() {
        SoftAssert as = new SoftAssert();
//        productPage = loginPage.validLogin(loginUsers.getJSONObject("validLogin").getString("username"), loginUsers.getJSONObject("validLogin").getString("password"));
        DeepLink.OpenAppWith("swaglabs://swag-overview/0,1");
        productPage = new productPage();
        String actualTxt = productPage.productName();
        String expectedTxt = "Sauce Labs Backpack";
        as.assertEquals(actualTxt,expectedTxt);

        String actualPri = productPage.productPri();
        String expectedPri = "$29.99";
        as.assertEquals(actualPri,expectedPri);

        settingPage = productPage.pressSettingButton();
        loginPage = settingPage.logout();

        as.assertAll();

    }

    @Test
    public void validProductOnProductDetailPage(){
        SoftAssert as = new SoftAssert();
        //productPage = loginPage.validLogin(loginUsers.getJSONObject("validLogin").getString("username"), loginUsers.getJSONObject("validLogin").getString("password"));
        DeepLink.OpenAppWith("swaglabs://swag-overview/0,1");
        productPage = new productPage();
        productDetailsPage = productPage.productDetail();
        String actualProductName = productDetailsPage.getProductName();
        String expectedProductName = "Sauce Labs Backpack";
        as.assertEquals(actualProductName,expectedProductName);


        productDetailsPage.ScrollingToAddToCart();
        String actualProductDescription = productDetailsPage.getProductDescription();
        String expectedProductDescription = "carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection.";
        as.assertEquals(actualProductDescription, expectedProductDescription);


        String actualPrice = productDetailsPage.getProductPrice();
        System.out.println("The actual Price: " + actualPrice);
        String expectedPrice = "$29.99";
        as.assertEquals(actualPrice,expectedPrice);

        System.out.println(productDetailsPage.addToCartDisplayed());
        as.assertTrue(productDetailsPage.addToCartDisplayed());

        productPage = productDetailsPage.backToProductPage();
        settingPage = productPage.pressSettingButton();
        loginPage = settingPage.logout();

        as.assertAll();


    }

}
