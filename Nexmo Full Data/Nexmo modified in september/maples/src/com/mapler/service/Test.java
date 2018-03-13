package com.mapler.service;

import com.mapler.utility.Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

public class Test {
    public static void main(String m[]) {
        InetAddress ip;
	try {
             String computername=InetAddress.getLocalHost().getHostName();
            System.out.println(computername+"::::::::::::::::: "+ Util.getSystemUUID());
	 // wmic command for diskdrive id: wmic DISKDRIVE GET SerialNumber
        // wmic command for cpu id : wmic cpu get ProcessorId
        /*Process process = Runtime.getRuntime().exec(new String[] { "wmic", "bios", "get", "serialnumber" });
        process.getOutputStream().close();
        Scanner sc = new Scanner(process.getInputStream());
        String property = sc.next();
        String serial = sc.next();
        System.out.println(property + ": " + serial);
        
            ip = InetAddress.getLocalHost();
		System.out.println("Current IP address : " + ip.getHostAddress());
 
		NetworkInterface network = NetworkInterface.getByInetAddress(ip);
 
		byte[] mac = network.getHardwareAddress();
 
		System.out.print("Current MAC address : ");
 
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
		}
		System.out.println(sb.toString());*/
 
	} catch (Throwable ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } 
        //try {
            
            //String home = System.getenv("ff.home");
            //System.out.println("home:::" + home);
            //home = System.getenv("ff_home");
            //System.out.println("home:::" + home);
            //System.setProperty("webdriver.firefox.bin", home);
            /*String CMD = "cmd /c rasdial.exe irobot 137472 m!thun";
             //String CMD = "cmd /c rasphone.exe -h office";
             //String CMD = "cmd /c rasdial.exe irobot /DISCONNECT";
             // Run "netsh" Windows command
             Process process = Runtime.getRuntime().exec(CMD);

             // Get input streams
             BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
             BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

             // Read command standard output
             String s;
             System.out.println("Standard output: ");
             while ((s = stdInput.readLine()) != null) {
             System.out.println(s);
             }

             // Read command errors
             System.out.println("Standard error: ");
             while ((s = stdError.readLine()) != null) {
             System.out.println(s);
             }*/
            /*String response = "<AccountResponse><errorcode>001</errorcode>"
             + "<message>Record found</message>"
             + "<accounts>"
             + "<account><id>2</id><name>prantoor</name>prantoor<password>prantoor</password></account>"
             + "<account><id>2</id><name>prantoor</name>prantoor<password>prantoor</password></account>"
             + "</accounts></AccountResponse>";
             Document document = DocumentHelper.parseText(response);
             List<Node> list = document.selectNodes("/AccountResponse/accounts/account");                            
             for (Node node : list) {
             System.out.println("::::::::::"+node.valueOf("id"));
             }*/
            /*for (Iterator iter = list.iterator(); iter.hasNext();) {
             Attribute attribute = (Attribute) iter.next();
             String url = attribute.getValue();
             System.out.println("::::::"+url);
             }*/
       // } catch (Throwable ex) {
      //      ex.printStackTrace();
      //  }

    }
}
