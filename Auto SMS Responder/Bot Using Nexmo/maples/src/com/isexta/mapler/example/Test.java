/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isexta.mapler.example;

import com.mapler.model.SModel;
import com.mapler.model.UAModel;
import com.mapler.utility.Util;
import com.sun.mail.iap.Protocol;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 *
 * @author Mithun
 */
public class Test {

    public void istest() {
        WebDriver driver = null;
        try {
            SModel sModel = new SModel();
            sModel.setDriver("FF");
            driver = Util.createDriver(sModel, new UAModel());
            String url = "http://www.freeonlinearticlespinner.com/";
            driver.get(url);

            WebElement elementSpunText = driver.findElement(By.id("spunText"));
            elementSpunText.sendKeys("None of the popular browsers uses the JavaScript engine used by HtmlUnit (Rhino). If you test JavaScript using HtmlUnit the results may differ significantly from those browsers."
                    + "When we say we actually mean “JavaScript and the DOM”. Although the DOM is defined by the W3C each browser has its own quirks and differences in their implementation of the DOM and in how JavaScript interacts with it. HtmlUnit has an impressively complete implementation of the DOM and has good support for using JavaScript, but it is no different from any other browser: it has its own quirks and differences from both the W3C standard and the DOM implementations of the major browsers, despite its ability to mimic other browsers."
                    + "With WebDriver, we had to make a choice; do we enable HtmlUnit’s JavaScript capabilities and run the risk of teams running into problems that only manifest themselves there, or do we leave JavaScript disabled, knowing that there are more and more sites that rely on JavaScript? We took the conservative approach, and by default have disabled support when we use HtmlUnit. With each release of both WebDriver and HtmlUnit, we reassess this decision: we hope to enable JavaScript by default on the HtmlUnit at some point.");
            WebElement elementJS = (WebElement) ((JavascriptExecutor) driver)
                    .executeScript("document.getElementById('spunText').select();");

            List<WebElement> elements = driver.findElements(By.tagName("a"));
            for (WebElement elementLink : elements) {
                if ((elementLink.getAttribute("onclick") != null && elementLink.getAttribute("onclick").contains("createSpunText"))) {
                    elementLink.click();
                    break;
                }
            }
            int tryin = 7;
            String spunText = "";
            for (int i = 0;; i++) {
                Util.wait(3);
                spunText = elementSpunText.getAttribute("value");
                if (!spunText.isEmpty()) {
                    break;
                }
                if (i == tryin) {
                    break;
                }
            }

            WebElement element = driver.findElement(By.id("spinText"));
            element.sendKeys(spunText);

            element = driver.findElement(By.id("button_silver"));
            element.click();

            String articleText = "";
            for (int i = 0;; i++) {
                try {
                    Util.wait(2);
                    driver.findElement(By.id("d_clip_button1")).click();
                    Util.wait(1);
                    WebElement articleEle = driver.findElement(By.id("articleText"));
                    articleText = articleEle.getAttribute("value");
                    if (!articleText.isEmpty()) {
                        break;
                    }
                    if (i == tryin) {
                        break;
                    }
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }

            }
            System.out.println("article :::" + articleText);
            //Util.wait(3);


        } catch (Throwable ex) {
            ex.printStackTrace();
            System.out.println("catch...");
        } finally {
            System.out.println("finally....");
        }
    }
    //static Logger log = Logger.getLogger(Test.class);

    private void deleteFXTmpDirectory() {
        try {
            File directory = new File("C:\\Users\\Mithun\\AppData\\Local\\Temp");
            File[] files = directory.listFiles();
            for (File file : files) {
                if (file.getName().endsWith("webdriver-profile")) {
                    try {
                        FileUtils.deleteDirectory(file);
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }

            }
            //FileUtils.deleteDirectory(directory);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String ss[]) {
        System.out.println(System.getenv("ff.tmpdir"));
        //new Test().istest();
        /*try {
            URLConnection connection = new URL("").openConnection();
            connection.connect();
            
            connection = new URL("").openConnection();
            connection.connect();
            
             System.out.println("true....");
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
         //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    /*try {
     //SModel sModel = new SModel();
     //sModel.setDriver("FF");
     //WebDriver driver = Util.createDriver(sModel);
     //driver.get("www.clirobott.com");
     /*String CMD;
     CMD = "cmd /c ping www.clirobott.com";
            
     // Run "netsh" Windows command
     Process process = Runtime.getRuntime().exec(CMD);
     // Get input streams
     BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
     String s;
     while ((s = stdInput.readLine()) != null) {
           
     System.out.println(":::::::::::::"+s);
     }
        
     } catch (Throwable e) {
     // TODO Auto-generated catch block
     e.printStackTrace();
     } finally {
     }*/
}
}
