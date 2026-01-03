package com.qa.utils;

import com.qa.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestUtils {
    public static final long WAIT = 10;

    public String dateTime(){ // Create the method for retriving the date time
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        return dateFormat.format(date);
    }

    public void log(String txt){//For output the log in separate device
        BaseTest base = new BaseTest();
        String msg = Thread.currentThread().threadId() + ":" + base.getPlatform() + ":" + base.getDeviceName() + ":"
                + Thread.currentThread().getStackTrace()[2].getClassName() + ":" + txt;
        System.out.println(msg);
        String strFile = "logs" + File.separator + base.getPlatform() + "_" + base.getDeviceName()
                + File.separator + base.getDateTime();
        File logFile = new File(strFile);

        if (!logFile.exists()){
            logFile.mkdirs();
        }

        FileWriter fileWriter = null;
        try{
            fileWriter = new FileWriter(logFile + File.separator + "log.txt", true);
        }catch (IOException e){
            e.printStackTrace();
        }
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println(msg);
        printWriter.close();
    }

    public Logger log(){
        return LogManager.getLogger(Thread.currentThread().getStackTrace()[2].getClassName());
    }
}
