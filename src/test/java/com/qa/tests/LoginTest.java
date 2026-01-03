package com.qa.tests;

import com.qa.BaseTest;
import com.qa.pages.loginPage;
import com.qa.pages.productPage;
import com.qa.utils.TestUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

public class LoginTest extends BaseTest { //Have to extends super class
    loginPage loginPage;
    productPage productPage;
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
    public void invalidUserName(){
            loginPage.enterUserName(loginUsers.getJSONObject("invalidUserName").getString("username"));//Get the data from Json
            loginPage.enterPassword(loginUsers.getJSONObject("invalidUserName").getString("password"));
            loginPage.loginAction();

            String actualErrTxt = loginPage.getErrorTxt();
            String expectedErrTxt = "Username and password do not match any user in this service.";
            Assert.assertEquals(actualErrTxt,expectedErrTxt);
    }

    @Test
    public void invalidPassword(){
        loginPage.enterUserName(loginUsers.getJSONObject("invalidPassword").getString("username"));
        loginPage.enterPassword(loginUsers.getJSONObject("invalidPassword").getString("password"));
        loginPage.loginAction();
        String actualErrTxt = loginPage.getErrorTxt();
        String expectErrTxt = "Username and password do not match any user in this service.";
        Assert.assertEquals(actualErrTxt,expectErrTxt);
    }

    @Test
    public void validLogin(){
        loginPage.enterUserName(loginUsers.getJSONObject("validLogin").getString("username"));
        loginPage.enterPassword(loginUsers.getJSONObject("validLogin").getString("password"));
        productPage = loginPage.loginAction();

        String actualProductTxt = productPage.productTxt();
        String expectProductTxt = "PRODUCTS";
        Assert.assertEquals(actualProductTxt,expectProductTxt);
    }

}
