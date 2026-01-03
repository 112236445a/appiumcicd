package com.qa.listeners;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.qa.BaseTest;
import com.qa.reports.ExtentReport;
import com.qa.utils.TestUtils;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class TestListener implements ITestListener {
    TestUtils utils = new TestUtils();


    public void onTestFailure(ITestResult result){//Create this method to print out the exception in console and apply it into XML <suit> level for all TC
        if(result.getThrowable() != null){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            result.getThrowable().printStackTrace(pw); //Using try catch block and e.printStackTrace() to print out the exception in console
             utils.log(sw.toString());//Can print the issue of the method into console
        }

        BaseTest base = new BaseTest();
        File file = base.getDriver().getScreenshotAs(OutputType.FILE);//.getScreenshotAs method need to be executed by driver. Create a method for getDriver in BaseTest

        byte[] encoded =null;
        try{
            encoded = Base64.getEncoder().encode(FileUtils.readFileToByteArray(file)); //Reading the file object by byte
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map <String, String> parms = new HashMap<String, String>();
        parms = result.getTestContext().getCurrentXmlTest().getAllParameters();

        String imagePath = "ScreenShots" + File.separator + parms.get("platformName") + "_" + parms.get("deviceName")
                + File.separator + base.getDateTime() //Getting the timestamp
                + File.separator + result.getTestClass().getRealClass().getSimpleName() //Getting the Test Class name
                + File.separator + result.getName() + ".png"; // Getting the Test Method name

        String completeImagePath = System.getProperty("user.dir") + File.separator + imagePath;

        try {
            FileUtils.copyFile(file,new File(imagePath));
            Reporter.log("This is the sample screenshot");//For insert the screenshot into the TestNG report
            Reporter.log("<a href='" + completeImagePath + "'>" + completeImagePath + "' height='100' width='100'/> </a>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ExtentReport.getTest().log(Status.FAIL, "Test Failed");
        //ExtentReport.getReporter().flush();
        // Can put in each method for demonstrating the log during the execution
        ExtentReport.getTest().fail("Test Failed", MediaEntityBuilder.createScreenCaptureFromPath(completeImagePath).build()); //For capture the screenshot and attach to the Spark report
        ExtentReport.getTest().fail("Test Failed", MediaEntityBuilder.createScreenCaptureFromBase64String(new String(encoded, StandardCharsets.US_ASCII)).build()); //Converting the byte array to string
        ExtentReport.getTest().fail(result.getThrowable());//Log the exception to extend report
    }

    @Override
    public void onTestStart(ITestResult result){
        BaseTest base = new BaseTest();
        ExtentReport.startTest(result.getName(), result.getMethod().getDescription())
                .assignCategory(base.getPlatform() + "_" + base.getDeviceName())
                .assignAuthor("Jacky");
    }

    @Override
    public void onTestSuccess(ITestResult result){
        ExtentReport.getTest().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestSkipped(ITestResult result){
        ExtentReport.getTest().log(Status.SKIP, "Test Skipped");
    }

    @Override
    public void onFinish(ITestContext context){
        ExtentReport.getReporter().flush();//Writes all the information to the report
    }
}
