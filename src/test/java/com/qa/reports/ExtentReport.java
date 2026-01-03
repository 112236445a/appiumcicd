package com.qa.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.util.HashMap;
import java.util.Map;

public class ExtentReport {
    static ExtentReports extent;
    final static String filePath = "Spark.html";
    static Map<Integer, ExtentTest> extentTestMap = new HashMap();

    public synchronized static ExtentReports getReporter(){ //Creating only one instance of the extent reports
        if (extent == null){
            ExtentSparkReporter spark = new ExtentSparkReporter("Spark.html");
            extent = new ExtentReports();
            spark.config().setDocumentTitle("Appium Framework");
            spark.config().setReportName("My App");
            spark.config().setTheme(Theme.DARK);
            extent.attachReporter(spark);//Attach the SparkReporter to the ExtentReports variable
        }
        return extent;
    }

    public synchronized static ExtentTest getTest(){
        return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().threadId()));//Using the Hashmap to retrieve the corresponding value
    }

    public synchronized static ExtentTest startTest(String testName, String desc){
        ExtentTest test = getReporter().createTest(testName, desc);//Initiate the instance for the extent report and pass to test object
        extentTestMap.put((int) (long) (Thread.currentThread().threadId()), test);//Pass the key:ThreadID and the value test object to Hashmap
        return test;
    }
}
