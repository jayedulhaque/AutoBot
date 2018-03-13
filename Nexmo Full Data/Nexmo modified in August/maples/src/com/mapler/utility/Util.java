package com.mapler.utility;

import com.mapler.model.AdModel;
import com.mapler.model.DataModel;
import com.mapler.model.SModel;
import com.mapler.model.UAModel;
import com.mapler.service.INotifier;
import com.opera.core.systems.OperaDriver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author none
 */
public class Util {

    private static Logger log = Logger.getLogger(Util.class);
    private static Random random;

    public enum CONNSTATUS {

        CONNECTED,
        DISCONNECTED,
        ALREADYCONNECTED,
        ERROR
    };

    public static boolean connect(String ip, String uName, String password) {
        Util.CONNSTATUS status = Util.connectVPN("C", ip, uName, password);
        if (status.equals(Util.CONNSTATUS.CONNECTED)) {
            return true;
        }

        if (status.equals(Util.CONNSTATUS.ALREADYCONNECTED)) {
            status = Util.connectVPN("D", ip, uName, password);
        }

        if (status.equals(Util.CONNSTATUS.DISCONNECTED)) {
            status = Util.connectVPN("C", ip, uName, password);
        }

        if (status.equals(Util.CONNSTATUS.ERROR)) {
            status = Util.connectVPN("C", ip, uName, password);
        }

        if (status.equals(Util.CONNSTATUS.CONNECTED)) {
            return true;
        }
        return false;
    }

    public static boolean disConnect(String ip, String uName, String password) {
        try {
            Util.CONNSTATUS status = Util.connectVPN("D", ip, uName, password);
            if (status.equals(Util.CONNSTATUS.DISCONNECTED)) {
                return true;
            }

            if (status.equals(Util.CONNSTATUS.ERROR)) {
                status = Util.connectVPN("D", ip, uName, password);
            }

            if (status.equals(Util.CONNSTATUS.DISCONNECTED)) {
                return true;
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
            log.debug("disConnect:: stopped causes " + ex);
        }
        return false;
    }

    public static CONNSTATUS connectVPN(String action, String ip, String uName, String password) {
        try {
            String CMD;
            if (action.equalsIgnoreCase("C")) {
                CMD = "cmd /c rasdial.exe irobot " + uName + " " + password + " /PHONE:" + ip;
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
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
            log.debug("connectVPN:: stopped causes " + ex);
        }
        return CONNSTATUS.ERROR;
    }

    public static void addPostInfo(INotifier iNotifier, SModel sModel, String link) {
        try {
            iNotifier.notify("Saving posting information.");
            log.debug("addPostInfo:: Saving posting confirmation link");

            StringBuilder request = new StringBuilder();
            request.append("<Request>");
            request.append("<username>").append(sModel.getUsername()).append("</username>");
            request.append("<password>").append(sModel.getPassword()).append("</password>");
            request.append("<country>").append(sModel.getCountry()).append("</country>");
            request.append("<link>").append(link).append("</link>");
            request.append("</Request>");

            String req = URLEncoder.encode(request.toString(), "UTF-8");
            String uri = "http://" + IConstant.HOSTNAME + "/index.php?r=postInfo/addPostInfo";
            String param = "request=" + req;
            String response = HttpHelper.post(new URL(uri), param);

            Document document = DocumentHelper.parseText(response);
            String errorCode = document.valueOf("Response/errorcode");
            String msg = document.valueOf("Response/message");

            log.debug("addPostInfo:: code :" + errorCode + " message:" + msg);

        } catch (Exception ex) {
            ex.printStackTrace();
            iNotifier.notify("addPostInfo:: Error on saving posting information " + ex);
            log.debug("addPostInfo:: stopped causes " + ex);
        }
    }

    public static WebDriver createDriver(SModel sModel, UAModel uAModel) {
        WebDriver driver = null;
        Proxy proxy = null;
        DesiredCapabilities capabilities = null;
        if (sModel.isBrowserProxy()) {
            proxy = new Proxy();
            if (sModel.getProxyType()) {
                proxy.setProxyType(Proxy.ProxyType.MANUAL);
            } else {
                proxy.setProxyType(Proxy.ProxyType.AUTODETECT);
            }
            if (sModel.isUseSystem()) {
                proxy.setHttpProxy(sModel.getProxyServer().trim() + ":" + sModel.getProxyPort());
            } else {
                proxy.setHttpProxy(uAModel.getProxyIp().trim() + ":" + uAModel.getProxyPort());
                proxy.setSslProxy(uAModel.getProxyIp().trim() + ":" + uAModel.getProxyPort());
                //proxy.setFtpProxy(uAModel.getProxyIp().trim() + ":" + uAModel.getProxyPort());
                if (uAModel.getProxyUsername() != null && !uAModel.getProxyUsername().isEmpty()) {
                    proxy.setSocksUsername(uAModel.getProxyUsername().trim());
                }
                if (uAModel.getProxyPassword() != null && !uAModel.getProxyPassword().isEmpty()) {
                    proxy.setSocksPassword(uAModel.getProxyPassword().trim());
                }

                //System.setProperty("http.proxyHost", uAModel.getProxyIp().trim());
                // System.setProperty("http.proxyPort", ""+uAModel.getProxyPort());
                //System.setProperty("http.proxyUser", uAModel.getProxyUsername().trim());
                //System.setProperty("http.proxyPassword", uAModel.getProxyPassword().trim());
            }
        }
        capabilities = new DesiredCapabilities();
        if (proxy != null) {
            capabilities.setCapability(CapabilityType.PROXY, proxy);
            //capabilities.setCapability("http.proxyUser", uAModel.getProxyUsername().trim());
            //capabilities.setCapability("http.proxyPassword", uAModel.getProxyPassword().trim());
        }

        if (sModel.getDriver().equalsIgnoreCase("IE")) {
            if (StringUtils.isNotBlank(sModel.getIePath())) {
                System.setProperty("webdriver.ie.driver", sModel.getIePath().trim());
            } else {
                String home = System.getenv("ie.home");
                if (home != null && !home.isEmpty()) {
                    System.setProperty("webdriver.ie.driver", home);
                }
            }

            capabilities = DesiredCapabilities.internetExplorer();
            capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
            capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
            capabilities.setCapability(InternetExplorerDriver.ENABLE_ELEMENT_CACHE_CLEANUP, true);

            //capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);  
            //capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);

            driver = new InternetExplorerDriver(capabilities);
        } else if (sModel.getDriver().equalsIgnoreCase("FF")) {
            if (StringUtils.isNotBlank(sModel.getFfPath())) {
                System.setProperty("webdriver.firefox.bin", sModel.getFfPath().trim());
            } else {
                String home = System.getenv("ff.home");
                if (home != null && !home.isEmpty()) {
                    System.setProperty("webdriver.firefox.bin", home);
                }
            }
            FirefoxProfile profile = new FirefoxProfile();

            if (sModel.isResourcesHide()) {
                // Disable CSS
                profile.setPreference("permissions.default.stylesheet", 2);

                // Disable images
                profile.setPreference("permissions.default.image", 2);
                // Disable Flash
                profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so", "false");
            }
            /*if (sModel.isBrowserProxy()) {
             if (sModel.getProxyType()) {
             profile.setPreference("network.proxy.type", 1);
             } else {
             profile.setPreference("network.proxy.type", 0);
             }
             if (sModel.isUseSystem()) {
             profile.setPreference("network.proxy.http", sModel.getProxyServer().trim());
             profile.setPreference("network.proxy.http_port", sModel.getProxyPort());
             } else {
             profile.setPreference("network.proxy.http", uAModel.getProxyIp().trim());
             profile.setPreference("network.proxy.http_port", uAModel.getProxyPort());
             }
             }*/
            driver = new FirefoxDriver(null, profile, capabilities);
            //driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
            //driver. manage().timeouts().setScriptTimeout(Integer.parseInt(sModel.getMaxWaitTime()), TimeUnit.SECONDS);
            //driver. manage().timeouts().pageLoadTimeout(Integer.parseInt(sModel.getMaxWaitTime()), TimeUnit.SECONDS);
        } else if (sModel.getDriver().equalsIgnoreCase("GC")) {
            if (StringUtils.isNotBlank(sModel.getFfPath())) {
                System.setProperty("webdriver.chrome.driver", sModel.getGcPath().trim());
            } else {
                String home = System.getenv("chrome.home");
                if (home != null && !home.isEmpty()) {
                    System.setProperty("webdriver.chrome.driver", home);
                }
            }
            driver = new ChromeDriver(capabilities);
        } else if (sModel.getDriver().equalsIgnoreCase("Safari")) {
            if (StringUtils.isNotBlank(sModel.getSafariPath())) {
              System.setProperty("OPERA_PATH", sModel.getSafariPath().trim());
            } else {
                String home = System.getenv("safari.home");
                if (home != null && !home.isEmpty()) {
                 System.setProperty("OPERA_PATH", home);
                }
            }
            // System.setProperty("webdriver.safari.noinstall", "false");
           driver = new OperaDriver();
           driver.manage().timeouts().implicitlyWait(10,  TimeUnit.SECONDS);
        }
        driver.manage().deleteAllCookies();
        return driver;
    }

    public static void quitDriver(WebDriver driver) {
        if (driver != null) {
            driver.quit();
        }
    }

    public static void wait(int sec) {
        try {
            if (sec <= 0) {
                return;
            }
            Thread.sleep(sec * 1000);
        } catch (Throwable ex) {
            log.debug("Error in waiting." + ex);
        }
    }
    
    public static void waitInMiliSecond(int sec) {
        try {
            if (sec <= 0) {
                return;
            }
            Thread.sleep(sec);
        } catch (Throwable ex) {
            log.debug("Error in waiting." + ex);
        }
    }
    
    public static void fireException() throws Exception {
        throw new Exception("Unable to find specific component or problem in execution.");
    }

    public static void fireException(String message) throws Exception {
        throw new Exception(message);
    }

    public static void byButton(WebDriver driver, String tName, String aName, String aValue) throws Exception {
        boolean isOff = true;
        List<WebElement> elements = driver.findElements(By.tagName(tName));
        for (WebElement elementLink : elements) {
            if (elementLink.getAttribute(aName) != null
                    && elementLink.getAttribute(aName).equalsIgnoreCase(
                    aValue)) {
                elementLink.submit();
                isOff = false;
                break;
            }
        }
        if (isOff) {
            Util.fireException();
        }
    }

    public static void byButton(WebDriver driver, String tId) throws Exception {
        WebElement element = driver.findElement(By.id(tId));
        element.submit();
    }

    public static void byClickName(WebDriver driver, String tName, String aName, String aValue) throws Exception {
        boolean isOff = true;
        List<WebElement> elements = driver.findElements(By.name(tName));
        for (WebElement elementLink : elements) {
            if (elementLink.getAttribute(aName) != null
                    && elementLink.getAttribute(aName).equalsIgnoreCase(
                    aValue)) {
                elementLink.click();
                isOff = false;
                break;
            }
        }
        if (isOff) {
            Util.fireException();
        }
    }

    public static void byClickTag(WebDriver driver, String tName, String aName, String aValue) throws Exception {
        boolean isOff = true;
        List<WebElement> elements = driver.findElements(By.tagName(tName));
        for (WebElement elementLink : elements) {
            if (elementLink.getAttribute(aName) != null
                    && elementLink.getAttribute(aName).equalsIgnoreCase(
                    aValue)) {
                elementLink.click();
                isOff = false;
                break;
            }
        }
        if (isOff) {
            Util.fireException();
        }
    }

    public static void byClickTag(WebDriver driver, String tID) throws Exception {
        WebElement element = driver.findElement(By.id(tID));
        if (element == null) {
            log.debug(tID + " element not found");
            Util.fireException();
        }
        element.click();
    }

    public static void sendKeysById(WebDriver driver, String aId, String aValue) throws Exception {
        WebElement element = driver.findElement(By.id(aId));
        if (element == null) {
            log.debug(aId + "element not found.");
            Util.fireException();
        }
        element.sendKeys(aValue);
    }

    public static void deleteFXTmpDirectory() {
        try {
            String home = System.getenv("ff.tmpdir");
            if (home == null || home.isEmpty()) {
                log.debug("Firefox temp directory is not set");
                return;
            }
            File directory = new File(home);
            File[] files = directory.listFiles();
            for (File file : files) {
                if (file.getName().endsWith("webdriver-profile")) {
                    try {
                        FileUtils.deleteDirectory(file);
                    } catch (Throwable ex) {
                        log.debug("Error on deleting directory named " + file.getName() + " Error " + ex);
                    }
                }

            }
        } catch (Throwable ex) {
            log.debug("Error on deleting directory " + ex);
        }
    }

    public static void updateWorking(SModel sModel, String clID, int work) {
        try {
            StringBuilder request = new StringBuilder();
            request.append("<Request>");
            request.append("<username>").append(sModel.getUsername()).append("</username>");
            request.append("<password>").append(sModel.getPassword()).append("</password>");
            request.append("<country>").append(sModel.getCountry()).append("</country>");
            request.append("<id>").append(clID).append("</id>");
            request.append("<working>").append(work).append("</working>");
            request.append("</Request>");

            String req = URLEncoder.encode(request.toString(), "UTF-8");
            String uri = "http://" + IConstant.HOSTNAME + "/index.php?r=account/updateWorking";
            String param = "request=" + req;
            String response = HttpHelper.post(new URL(uri), param);

            Document document = DocumentHelper.parseText(response);
            String errorCode = document.valueOf("Response/errorcode");
            String msg = document.valueOf("Response/message");
            log.debug("updateWorking:: update " + msg);
        } catch (Throwable ex) {
            log.debug("updateWorking: Error " + ex);
        }
    }

    public static void updateNGFXWorking(SModel sModel, String clID, int work) {
        try {
            StringBuilder request = new StringBuilder();
            request.append("<Request>");
            request.append("<username>").append(sModel.getUsername()).append("</username>");
            request.append("<password>").append(sModel.getPassword()).append("</password>");
            request.append("<country>").append(sModel.getCountry()).append("</country>");
            request.append("<id>").append(clID).append("</id>");
            request.append("<working>").append(work).append("</working>");
            request.append("</Request>");

            String req = URLEncoder.encode(request.toString(), "UTF-8");
            String uri = "http://" + IConstant.HOSTNAME + "/index.php?r=ngfxaccount/updateWorking";
            String param = "request=" + req;
            String response = HttpHelper.post(new URL(uri), param);

            Document document = DocumentHelper.parseText(response);
            String errorCode = document.valueOf("Response/errorcode");
            String msg = document.valueOf("Response/message");
            log.debug("updateWorking:: update " + msg);
        } catch (Throwable ex) {
            log.debug("updateWorking: Error " + ex);
        }
    }

    public static void updateNetellerWorking(SModel sModel, String clID, int work) {
        try {
            StringBuilder request = new StringBuilder();
            request.append("<Request>");
            request.append("<username>").append(sModel.getUsername()).append("</username>");
            request.append("<password>").append(sModel.getPassword()).append("</password>");
            request.append("<country>").append(sModel.getCountry()).append("</country>");
            request.append("<id>").append(clID).append("</id>");
            request.append("<working>").append(work).append("</working>");
            request.append("</Request>");

            String req = URLEncoder.encode(request.toString(), "UTF-8");
            String uri = "http://" + IConstant.HOSTNAME + "/index.php?r=netellerAccount/updateWorking";
            String param = "request=" + req;
            String response = HttpHelper.post(new URL(uri), param);

            Document document = DocumentHelper.parseText(response);
            String errorCode = document.valueOf("Response/errorcode");
            String msg = document.valueOf("Response/message");
            log.debug("updateWorking:: update " + msg);
        } catch (Throwable ex) {
            log.debug("updateWorking: Error " + ex);
        }
    }

    public static Message[] readGmail(String email, String emailPass, INotifier iNotifier) throws Exception {
        try {
            if (email.contains("+")) {
                String[] ee = email.split("\\+");
                email = ee[0] + "@gmail.com";
            }

            if (email == null || email.isEmpty()) {
                log.debug("run:: Email address is empty. ");
                Util.fireException("Email address is empty.");
            }

            if (emailPass == null || emailPass.isEmpty()) {
                log.debug("run:: Email password is empty. ");
                Util.fireException("Email password is empty.");
            }

            Properties props = System.getProperties();
            props.setProperty("mail.store.protocol", "imaps");

            /* Create the session and get the store for read the mail. */
            //iNotifier.notify("Going to connect mail server");
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", email, emailPass);

            /* Mention the folder name which you want to read. */
            Folder inbox = store.getFolder("Inbox");
            int unReadMCount = inbox.getUnreadMessageCount();
            //iNotifier.notify("No of Unread Messages : " + unReadMCount);
            if (unReadMCount == 0) {
                Util.fireException("No of Unread Messages : " + unReadMCount);
            }
            /* Open the inbox using store. */
            //inbox.open(Folder.READ_ONLY);
            inbox.open(Folder.READ_WRITE);
            /* Get the messages which is unread in the Inbox */
            Message messages[] = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            /* Use a suitable FetchProfile */
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            fp.add(FetchProfile.Item.CONTENT_INFO);
            inbox.fetch(messages, fp);

            try {
                inbox.setFlags(messages, new Flags(Flags.Flag.SEEN), true);
                inbox.close(true);
                store.close();
            } catch (Throwable ex) {
                log.debug("readMail :: unable to close inbox " + ex);
                //iNotifier.notify("Unable to close inbox");
                Util.fireException("readMail :: unable to close inbox " + ex);
            }
            return messages;
        } catch (NoSuchProviderException ex) {
            log.debug("readMail :: Error on NoSuchProviderException " + ex);
            //iNotifier.notify("Exception arise at the time of read mail");
            Util.fireException("readMail :: Error on NoSuchProviderException " + ex);
        } catch (MessagingException ex) {
            log.debug("readMail :: Error on MessagingException " + ex);
            //iNotifier.notify("Error on MessagingException "+ex);
            Util.fireException("readMail :: Error on MessagingException " + ex);
        } catch (Throwable ex) {
            log.debug("readMail :: Exception arise at the time of read mai " + ex);
            //iNotifier.notify("Exception arise at the time of read mail");
            Util.fireException("readMail :: Exception arise at the time of read mail " + ex);
        }
        return null;
    }

    public static List<Node> readWebGmail(String email, String emailPass, String wsName, INotifier iNotifier, SModel sModel) throws Exception {
        try {
            if (email.contains("+")) {
                String[] ee = email.split("\\+");
                email = ee[0] + "@gmail.com";
            }

            if (email == null || email.isEmpty()) {
                log.debug("run:: Email address is empty. ");
                Util.fireException("Email address is empty.");
            }

            if (emailPass == null || emailPass.isEmpty()) {
                log.debug("run:: Email password is empty. ");
                Util.fireException("Email password is empty.");
            }

            StringBuilder param = new StringBuilder();
            param.append("username=").append(sModel.getUsername());
            param.append("&password=").append(sModel.getPassword());
            param.append("&email=").append(email);
            param.append("&epassword=").append(emailPass);

            String uri = "http://" + IConstant.WEBHOSTNAME + "/maplew/" + wsName;
            String response = HttpHelper.post(new URL(uri), param.toString());

            Document document = DocumentHelper.parseText(response);
            String errorCode = document.valueOf("Response/errorcode");
            String msg = document.valueOf("Response/message");
            if (!errorCode.equalsIgnoreCase("000")) {
                String isLink = document.valueOf("Response/links/mailfound");
                if (isLink.equals("0")) {
                    iNotifier.notify("Posting link not found");
                    Util.fireException("Posting link not found");
                }
                return document.selectNodes("/Response/links/link");
            } else {
                iNotifier.notify("Error " + msg);
                Util.fireException("Error on service call " + msg);
            }
        } catch (Throwable ex) {
            log.debug("updateWorking: Error " + ex);
            Util.fireException("Error on service call " + ex.getMessage());
        }
        return null;
    }

    public static Random getRandom() {
        if (random == null) {
            random = new Random();
        }
        return random;
    }

    public static String getInetAddress() {
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            return sb.toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getSystemUUID() {
        String uuid = "";
        try {
            // wmic DISKDRIVE get SerialNumber | wmic csproduct get uuid
            //Process process = Runtime.getRuntime().exec(new String[] { "wmic", "bios", "get", "serialnumber" });
            Process process = Runtime.getRuntime().exec(new String[]{"wmic", "csproduct", "get", "UUID"});
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            String property = sc.next();
            uuid = sc.next();
        } catch (Throwable ex) {
            log.error("getMacAddress: " + ex.getMessage());
            ex.printStackTrace();
        }
        return uuid;
    }

    public static boolean isLoginByMac(String mac) {
        try {
            String uri = "http://" + IConstant.HOSTNAME + "/index.php?r=userAccount/isLoginByMac&mac=" + mac;
            String response = HttpHelper.post(new URL(uri), "");

            Document document = DocumentHelper.parseText(response);
            String message = document.valueOf("Response/message");
            if (message.contains("login")) {
                return true;
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
        }
        return false;
    }

    /*
     * 0 means only one irobot process alive
     * 1 means only one java process alive
     * 2 means only multiple irobot process alive
     * 3 means multiple java process alive
     */
    public static int isAppsRunning() {
        try {
            int countJavaP = 0;
            int countIRobotP = 0;
            String line;
            Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                if (line.toLowerCase().contains("irobot")) {
                    countIRobotP = countIRobotP + 1;
                }
                if (line.toLowerCase().contains("java.exe") || line.toLowerCase().contains("javaw.exe")) {
                    countJavaP = countJavaP + 1;
                }
            }
            input.close();
            if (countIRobotP == 0) {
                return 0;
            } else if (countIRobotP == 1 && countJavaP <= 3) {
                return 1;
            } else if (countJavaP == 1) {
                return 2;
            } else if (countIRobotP > 1) {
                return 3;
            } else if (countJavaP > 3) {
                return 5;
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return 5;
    }

    public ArrayList<DataModel> readFile(String filePath, INotifier iNotifier) throws Exception {
        ArrayList<DataModel> data = new ArrayList<DataModel>();
        BufferedReader br = null;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(filePath));
            while ((sCurrentLine = br.readLine()) != null) {
                DataModel dataModel = new DataModel();
                String[] aButes = sCurrentLine.split("\\|");
                if (aButes.length == 1) {
                    dataModel.setCode(aButes[0]);
                }
                if (aButes.length == 1) {
                    dataModel.setName(aButes[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            iNotifier.notify("readfile Stopped for error: " + e.getMessage());
            log.error("readfile Stopped causes..." + e);
            throw new Exception(e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return data;
    }

    public static boolean deleteAdd(SModel _sModel, AdModel adModel, INotifier iNotifier) {
        try {
            StringBuilder request = new StringBuilder();
            request.append("<Request>");
            request.append("<id>").append(adModel.getId()).append("</id>");
            request.append("<username>").append(_sModel.getUsername()).append("</username>");
            request.append("<password>").append(_sModel.getPassword()).append("</password>");
            request.append("</Request>");

            String uri = "http://" + IConstant.HOSTNAME + "/index.php?r=advertisement/deleteAdvertisement&request=" + request.toString();

            String response = HttpHelper.post(new URL(uri), "");

            Document document = DocumentHelper.parseText(response);
            String errorCode = document.valueOf("Response/errorcode");
            String msg = document.valueOf("Response/message");
            if (errorCode.equalsIgnoreCase("000")) {
                iNotifier.notify("Unable to delete the post.");
                return false;
            } else {
                iNotifier.notify("Post deleted successfully!");
                return true;
            }
        } catch (Exception ex) {
            log.debug("Error " + ex);
        }
        return false;
    }
}
