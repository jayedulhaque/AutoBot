package com.mapler.model;

import java.util.ArrayList;

/**
 *
 * @author none
 */
public class SModel {
    private String recordId;
    private String driver;
    private boolean isVPN;
    private boolean webMail;
    private boolean ignoreBannedCheck;
    private boolean ignoreLoginCheck;
    private boolean stopEngine;
    private boolean logout;
    private String accountID;
    private String autoPost;
    private String changeIP;
    private String countryUI;
    private String logAutoScroll;
    private String logincl;
    private String lwage;
    private String maxWaitTime;
    private String name;
    private String pass;
    private String postInterval;
    private String postPerIP;
    private String postType;
    private String postfix;
    private String prefix;
    private String randomSubarea;
    private String upage;
    private String useAge;
    private String useFWlink;
    private String useLocation;
    private String useProxy;
    private String maxTry;
    private String vpnName;
    private String link;
    private String proxy;
    private Integer postPerAccout;
    private String spinner;
    private String ProxyServer;
    private int ProxyPort;
    private boolean ProxyType;
    private String username;
    private String password;
    private String country;
    private Integer tryCount;
    private boolean browserProxy;
    private String connectMode;
    private String ips;
    private boolean random;
    private boolean byState;
    private boolean byCity;
    private boolean closeBrowser = false;
    private int prWaitTime = 0;
    private boolean resourcesHide = true;
    private boolean closeJustAfterLoad = true;
    private ArrayList<String> links;
    private ArrayList<String> proxyes;
    private String renewItems;
    private boolean deletePost;
    private int postCount;
    private boolean useSystem;
    private boolean updateAccount;
    private boolean postInSession;
    private String customPostLink;
    private String imageLinkFile;
    private String iePath;
    private String ffPath;
    private String gcPath;
    private String safariPath;
    private boolean useSlot;
    private int slotNo = 0;
    private String job;
    private boolean runIndividually;
    private String domainInfo;
    private int emailReadTry = 0;
    private String email;
    private String emailPassword;
    private boolean store;
    private int limit;
    private boolean voice = false;
    private boolean repeated;
    private String mailServer;
    private String protocal;
    private boolean mailReadAndUpdateOnly;
    private boolean mailUpdateOnly;

    private String[] sendHubRepeatTime;
    private String[] sendHubWaiTimeBeforeSend;
    private int sendHubSendLimit = 1;
    String downloadOptions;
    private boolean scrapperUpdateInServer;
    private boolean scrapperOnlyUpdate = false;
    private boolean useAlternateEmail = false;
    private int alternateEmailSize;
    private boolean autoResponderReadContinue;
    private int autoResponderBotFor;
    private int autoResponderSendEmailToServerLimit;
    private String messageFilePath;
    private int responseProtocalNo;
    private String alternativEmailFilePath;
    private int autoResponderCodeBotEmailSendLimit = 10;
    private long autoResponderCodeBotTimeLimit = 86400000;
    private boolean runAsInternal;
    private long autoResponderMaxAllowLimit = 0;
    private boolean useReInEmail;

    public boolean isUseReInEmail() {
        return useReInEmail;
    }
    
    public void setUseReInEmail(boolean useReInEmail) {
        this.useReInEmail = useReInEmail;
    }
    
    public long getAutoResponderMaxAllowLimit() {
        return autoResponderMaxAllowLimit;
    }

    public void setAutoResponderMaxAllowLimit(long autoResponderMaxAllowLimit) {
        this.autoResponderMaxAllowLimit = autoResponderMaxAllowLimit;
    }
    
    public boolean isRunAsInternal() {
        return runAsInternal;
    }

    public void setRunAsInternal(boolean runAsInternal) {
        this.runAsInternal = runAsInternal;
    }
    
    public int getAutoResponderCodeBotEmailSendLimit() {
        return autoResponderCodeBotEmailSendLimit;
    }

    public void setAutoResponderCodeBotEmailSendLimit(int autoResponderCodeBotEmailSendLimit) {
        this.autoResponderCodeBotEmailSendLimit = autoResponderCodeBotEmailSendLimit;
    }

    public long getAutoResponderCodeBotTimeLimit() {
        return autoResponderCodeBotTimeLimit;
    }

    public void setAutoResponderCodeBotTimeLimit(long autoResponderCodeBotTimeLimit) {
        this.autoResponderCodeBotTimeLimit = autoResponderCodeBotTimeLimit;
    }
    
    public String getAlternativEmailFilePath() {
        return alternativEmailFilePath;
    }

    public void setAlternativEmailFilePath(String alternativEmailFilePath) {
        this.alternativEmailFilePath = alternativEmailFilePath;
    }   
    
    public int getResponseProtocalNo() {
        return responseProtocalNo;
    }

    public void setResponseProtocalNo(int responseProtocalNo) {
        this.responseProtocalNo = responseProtocalNo;
    }   

    public String getMessageFilePath() {
        return messageFilePath;
    }

    public void setMessageFilePath(String messageFilePath) {
        this.messageFilePath = messageFilePath;
    }

   
    
    public int getAutoResponderSendEmailToServerLimit() {
        return autoResponderSendEmailToServerLimit;
    }

    public void setAutoResponderSendEmailToServerLimit(int autoResponderSendEmailToServerLimit) {
        this.autoResponderSendEmailToServerLimit = autoResponderSendEmailToServerLimit;
    }
    
    
    public int getAutoResponderBotFor() {
        return autoResponderBotFor;
    }

    public void setAutoResponderBotFor(int autoResponderBotFor) {
        this.autoResponderBotFor = autoResponderBotFor;
    }
    private boolean autoResponderSendContinue;

    public boolean isAutoResponderReadContinue() {
        return autoResponderReadContinue;
    }

    public void setAutoResponderReadContinue(boolean autoResponderReadContinue) {
        this.autoResponderReadContinue = autoResponderReadContinue;
    }

    public boolean isAutoResponderSendContinue() {
        return autoResponderSendContinue;
    }

    public void setAutoResponderSendContinue(boolean autoResponderSendContinue) {
        this.autoResponderSendContinue = autoResponderSendContinue;
    }

    public int getAlternateEmailSize() {
        return alternateEmailSize;
    }

    public void setAlternateEmailSize(int alternateEmailSize) {
        this.alternateEmailSize = alternateEmailSize;
    }

    public boolean isUseAlternateEmail() {
        return useAlternateEmail;
    }

    public void setUseAlternateEmail(boolean useAlternateEmail) {
        this.useAlternateEmail = useAlternateEmail;
    }

    public boolean isScrapperOnlyUpdate() {
        return scrapperOnlyUpdate;
    }

    public void setScrapperOnlyUpdate(boolean scrapperOnlyUpdate) {
        this.scrapperOnlyUpdate = scrapperOnlyUpdate;
    }

    public boolean isScrapperUpdateInServer() {
        return scrapperUpdateInServer;
    }

    public void setScrapperUpdateInServer(boolean scrapperUpdateInServer) {
        this.scrapperUpdateInServer = scrapperUpdateInServer;
    }

    public String getDownloadOptions() {
        return downloadOptions;
    }

    public void setDownloadOptions(String downloadOptions) {
        this.downloadOptions = downloadOptions;
    }

    public int getSendHubSendLimit() {
        return sendHubSendLimit;
    }

    public void setSendHubSendLimit(int sendHubSendLimit) {
        this.sendHubSendLimit = sendHubSendLimit;
    }

    public String[] getSendHubRepeatTime() {
        return sendHubRepeatTime;
    }

    public void setSendHubRepeatTime(String[] sendHubRepeatTime) {
        this.sendHubRepeatTime = sendHubRepeatTime;
    }

    public String[] getSendHubWaiTimeBeforeSend() {
        return sendHubWaiTimeBeforeSend;
    }

    public void setSendHubWaiTimeBeforeSend(String[] sendHubWaiTimeBeforeSend) {
        this.sendHubWaiTimeBeforeSend = sendHubWaiTimeBeforeSend;
    }

    public boolean isMailUpdateOnly() {
        return mailUpdateOnly;
    }

    public void setMailUpdateOnly(boolean mailUpdateOnly) {
        this.mailUpdateOnly = mailUpdateOnly;
    }

    public boolean isMailReadAndUpdateOnly() {
        return mailReadAndUpdateOnly;
    }

    public void setMailReadAndUpdateOnly(boolean mailReadAndUpdateOnly) {
        this.mailReadAndUpdateOnly = mailReadAndUpdateOnly;
    }

    public String getMailServer() {
        return mailServer;
    }

    public void setMailServer(String mailServer) {
        this.mailServer = mailServer;
    }

    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }

    public boolean isRepeated() {
        return repeated;
    }

    public void setRepeated(boolean repeated) {
        this.repeated = repeated;
    }

    public boolean isVoice() {
        return voice;
    }

    public void setVoice(boolean voice) {
        this.voice = voice;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isStore() {
        return store;
    }

    public void setStore(boolean store) {
        this.store = store;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public int getEmailReadTry() {
        return emailReadTry;
    }

    public void setEmailReadTry(int emailReadTry) {
        this.emailReadTry = emailReadTry;
    }

    public boolean isRunIndividually() {
        return runIndividually;
    }

    public void setRunIndividually(boolean runIndividually) {
        this.runIndividually = runIndividually;
    }

    public String getDomainInfo() {
        return domainInfo;
    }

    public void setDomainInfo(String domainInfo) {
        this.domainInfo = domainInfo;
    }

    public String getSafariPath() {
        return safariPath;
    }

    public void setSafariPath(String safariPath) {
        this.safariPath = safariPath;
    }

    public int getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(int slotNo) {
        this.slotNo = slotNo;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getAutoPost() {
        return autoPost;
    }

    public void setAutoPost(String autoPost) {
        this.autoPost = autoPost;
    }

    public String getChangeIP() {
        return changeIP;
    }

    public void setChangeIP(String changeIP) {
        this.changeIP = changeIP;
    }

    public String getCountryUI() {
        return countryUI;
    }

    public void setCountryUI(String countryUI) {
        this.countryUI = countryUI;
    }

    public String getLogAutoScroll() {
        return logAutoScroll;
    }

    public void setLogAutoScroll(String logAutoScroll) {
        this.logAutoScroll = logAutoScroll;
    }

    public String getLogincl() {
        return logincl;
    }

    public void setLogincl(String logincl) {
        this.logincl = logincl;
    }

    public String getLwage() {
        return lwage;
    }

    public void setLwage(String lwage) {
        this.lwage = lwage;
    }

    public String getMaxWaitTime() {
        return maxWaitTime;
    }

    public void setMaxWaitTime(String maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPostInterval() {
        return postInterval;
    }

    public void setPostInterval(String postInterval) {
        this.postInterval = postInterval;
    }

    public String getPostPerIP() {
        return postPerIP;
    }

    public void setPostPerIP(String postPerIP) {
        this.postPerIP = postPerIP;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostfix() {
        return postfix;
    }

    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getRandomSubarea() {
        return randomSubarea;
    }

    public void setRandomSubarea(String randomSubarea) {
        this.randomSubarea = randomSubarea;
    }

    public String getUpage() {
        return upage;
    }

    public void setUpage(String upage) {
        this.upage = upage;
    }

    public String getUseAge() {
        return useAge;
    }

    public void setUseAge(String useAge) {
        this.useAge = useAge;
    }

    public String getUseFWlink() {
        return useFWlink;
    }

    public void setUseFWlink(String useFWlink) {
        this.useFWlink = useFWlink;
    }

    public String getUseLocation() {
        return useLocation;
    }

    public void setUseLocation(String useLocation) {
        this.useLocation = useLocation;
    }

    public String getUseProxy() {
        return useProxy;
    }

    public void setUseProxy(String useProxy) {
        this.useProxy = useProxy;
    }

    public boolean isIsVPN() {
        return isVPN;
    }

    public void setIsVPN(boolean isVPN) {
        this.isVPN = isVPN;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getVpnName() {
        return vpnName;
    }

    public void setVpnName(String vpnName) {
        this.vpnName = vpnName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMaxTry() {
        return maxTry;
    }

    public void setMaxTry(String maxTry) {
        this.maxTry = maxTry;
    }

    public Integer getTryCount() {
        return tryCount;
    }

    public void setTryCount(Integer tryCount) {
        this.tryCount = tryCount;
    }

    public boolean isWebMail() {
        return webMail;
    }

    public void setWebMail(boolean webMail) {
        this.webMail = webMail;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public Integer getPostPerAccout() {
        return postPerAccout;
    }

    public void setPostPerAccout(Integer postPerAccout) {
        this.postPerAccout = postPerAccout;
    }

    public String getSpinner() {
        return spinner;
    }

    public void setSpinner(String spinner) {
        this.spinner = spinner;
    }

    public boolean isIgnoreBannedCheck() {
        return ignoreBannedCheck;
    }

    public void setIgnoreBannedCheck(boolean ignoreBannedCheck) {
        this.ignoreBannedCheck = ignoreBannedCheck;
    }

    public boolean isStopEngine() {
        return stopEngine;
    }

    public void setStopEngine(boolean stopEngine) {
        this.stopEngine = stopEngine;
    }

    public boolean isIgnoreLoginCheck() {
        return ignoreLoginCheck;
    }

    public void setIgnoreLoginCheck(boolean ignoreLoginCheck) {
        this.ignoreLoginCheck = ignoreLoginCheck;
    }

    public boolean isLogout() {
        return logout;
    }

    public void setLogout(boolean logout) {
        this.logout = logout;
    }

    public String getProxyServer() {
        return ProxyServer;
    }

    public void setProxyServer(String ProxyServer) {
        this.ProxyServer = ProxyServer;
    }

    public int getProxyPort() {
        return ProxyPort;
    }

    public void setProxyPort(int ProxyPort) {
        this.ProxyPort = ProxyPort;
    }

    public boolean getProxyType() {
        return ProxyType;
    }

    public void setProxyType(boolean ProxyType) {
        this.ProxyType = ProxyType;
    }

    public boolean isBrowserProxy() {
        return browserProxy;
    }

    public void setBrowserProxy(boolean browserProxy) {
        this.browserProxy = browserProxy;
    }

    public String getConnectMode() {
        return connectMode;
    }

    public void setConnectMode(String connectMode) {
        this.connectMode = connectMode;
    }

    public String getIps() {
        return ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }

    public boolean isRandom() {
        return random;
    }

    public void setRandom(boolean random) {
        this.random = random;
    }

    public boolean isByState() {
        return byState;
    }

    public void setByState(boolean byState) {
        this.byState = byState;
    }

    public boolean isByCity() {
        return byCity;
    }

    public void setByCity(boolean byCity) {
        this.byCity = byCity;
    }

    public int getPrWaitTime() {
        return prWaitTime;
    }

    public void setPrWaitTime(int prWaitTime) {
        this.prWaitTime = prWaitTime;
    }

    public boolean isCloseBrowser() {
        return closeBrowser;
    }

    public void setCloseBrowser(boolean closeBrowser) {
        this.closeBrowser = closeBrowser;
    }

    public boolean isResourcesHide() {
        return resourcesHide;
    }

    public void setResourcesHide(boolean resourcesHide) {
        this.resourcesHide = resourcesHide;
    }

    public boolean isCloseJustAfterLoad() {
        return closeJustAfterLoad;
    }

    public void setCloseJustAfterLoad(boolean closeJustAfterLoad) {
        this.closeJustAfterLoad = closeJustAfterLoad;
    }

    public ArrayList<String> getLinks() {
        if (links == null) {
            links = new ArrayList<String>();
        }
        return links;
    }

    public void setLinks(ArrayList<String> links) {
        this.links = links;
    }

    public ArrayList<String> getProxyes() {
        if (proxyes == null) {
            proxyes = new ArrayList<String>();
        }
        return proxyes;
    }

    public void setProxyes(ArrayList<String> proxyes) {
        this.proxyes = proxyes;
    }

    public String getRenewItems() {
        return renewItems;
    }

    public void setRenewItems(String renewItems) {
        this.renewItems = renewItems;
    }

    public boolean isDeletePost() {
        return deletePost;
    }

    public void setDeletePost(boolean deletePost) {
        this.deletePost = deletePost;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public boolean isUseSystem() {
        return useSystem;
    }

    public void setUseSystem(boolean useSystem) {
        this.useSystem = useSystem;
    }

    public boolean isUpdateAccount() {
        return updateAccount;
    }

    public void setUpdateAccount(boolean updateAccount) {
        this.updateAccount = updateAccount;
    }

    public boolean isPostInSession() {
        return postInSession;
    }

    public void setPostInSession(boolean postInSession) {
        this.postInSession = postInSession;
    }

    public String getCustomPostLink() {
        return customPostLink;
    }

    public void setCustomPostLink(String customPostLink) {
        this.customPostLink = customPostLink;
    }

    public String getImageLinkFile() {
        return imageLinkFile;
    }

    public void setImageLinkFile(String imageLinkFile) {
        this.imageLinkFile = imageLinkFile;
    }

    public String getIePath() {
        return iePath;
    }

    public void setIePath(String iePath) {
        this.iePath = iePath;
    }

    public String getFfPath() {
        return ffPath;
    }

    public void setFfPath(String ffPath) {
        this.ffPath = ffPath;
    }

    public String getGcPath() {
        return gcPath;
    }

    public void setGcPath(String gcPath) {
        this.gcPath = gcPath;
    }

    public boolean isUseSlot() {
        return useSlot;
    }

    public void setUseSlot(boolean useSlot) {
        this.useSlot = useSlot;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}