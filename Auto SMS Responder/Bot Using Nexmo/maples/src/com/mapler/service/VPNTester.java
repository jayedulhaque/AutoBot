package com.mapler.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 *
 * @author none
 */
public class VPNTester {

    public enum CONNSTATUS {

        CONNECTED,
        DISCONNECTED,
        ALREADYCONNECTED,
        ERROR
    };

    private CONNSTATUS connectVPN(String action) {
        try {
            String CMD;
            if (action.equalsIgnoreCase("C")) {
                //CMD = "cmd /c rasdial.exe irobot 137472 m!thun /DOMAIN:202.168.224.5";
                CMD = "cmd /c rasdial.exe irobot ukdyn227 841065 /PHONE:78.129.222.149";
            } else {
                //CMD = "cmd /c rasphone.exe -h office";
                CMD = "cmd /c rasdial.exe irobot /DISCONNECT";
            }
            // Run "netsh" Windows command
            Process process = Runtime.getRuntime().exec(CMD);
            // Get input streams
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // Read command standard output            
            String s;
            while ((s = stdInput.readLine()) != null) {
                if (s.contains("Successfully connected to")) {
                    return CONNSTATUS.CONNECTED;
                } else if (s.contains("You are already connected")) {
                    return CONNSTATUS.ALREADYCONNECTED;
                } else if (s.contains("Remote Access error")) {
                    return CONNSTATUS.ERROR;
                } else if (s.contains("Command completed successfully.")) {
                    return CONNSTATUS.DISCONNECTED;
                }
                System.out.println(s);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return CONNSTATUS.CONNECTED;
    }

    public static void main(String s[]) {
        System.out.println(new VPNTester().connectVPN("C").toString());
        //System.out.println(new VPNTester().connectVPN("D").toString());
    }
}
