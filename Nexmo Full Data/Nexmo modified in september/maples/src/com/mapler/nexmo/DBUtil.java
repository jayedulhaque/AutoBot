package com.mapler.nexmo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author JAYED
 */
public class DBUtil {

    private static final Logger log = Logger.getLogger(DBUtil.class);
    //static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
     static final String DB_URL = "jdbc:mysql://ns1.selvesperer.com:3306/clrobot_nexmo";
    //static final String DB_URL = "jdbc:mysql://localhost:3306/nexmo_demo";
    // Database credentials
    static final String USER = "clrobot_root";
    static final String PASS = "root_12";
//    static final String USER = "root";
//    static final String PASS = "root";
    public Connection connection;

    public Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            // STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            // STEP 3: Open a connection
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            return connection;
        } catch (Throwable ex) {
            if (log.isDebugEnabled()) {
                log.debug(ExceptionUtils.getMessage(ex));
            }
            log.error(ExceptionUtils.getMessage(ex));
        }
        return connection;
    }

    public void insertData(int status, int send_count, String message_id, String type, String from, String to, String account_id, String body, String date_recieved, String network, String user_id, long recievedTime, long nextSendTime, String api_key, String api_secret, long botSendingTime) {
        Connection _connection = this.getConnection();
        PreparedStatement statement = null;
        try {
            java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
            String dateInString = NexmoUtil.getCurrentTimeInUTCDate(date);
            String query = "INSERT INTO inbound_message"
                    + "(creation_date,message_status,send_count,message_id,account_id,message_from,message_to,body,date_recieved,message_type,network,user_id,next_send_time,recieved_time,api_key,api_secret,last_send_time,status_code) VALUES"
                    + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            statement = _connection.prepareStatement(query);
            statement.setString(1, dateInString);
            statement.setInt(2, status);
            statement.setInt(3, send_count);
            statement.setString(4, message_id);
            statement.setString(5, account_id);
            statement.setString(6, from);
            statement.setString(7, to);
            statement.setString(8, body);
            statement.setString(9, date_recieved);
            statement.setString(10, type);
            statement.setString(11, network);
            statement.setString(12, user_id);
            statement.setLong(13, nextSendTime);
            statement.setLong(14, recievedTime);
            statement.setString(15, api_key);
            statement.setString(16, api_secret);
            statement.setLong(17, botSendingTime);
            statement.setString(18, "");
            statement.executeUpdate();

        } catch (Throwable ex) {
            if (log.isDebugEnabled()) {
                log.debug(ExceptionUtils.getMessage(ex));
            }
            log.error(ExceptionUtils.getMessage(ex));
        } finally {
            this.closePreparedStatement(statement);
        }
    }

    private void closePreparedStatement(PreparedStatement statement) {
        try {
            if (statement == null || statement.isClosed()) {
                log.error("Statement is null or already closde.");
                return;
            }
            statement.close();
        } catch (Throwable ex) {
            if (log.isDebugEnabled()) {
                log.debug(ExceptionUtils.getMessage(ex));
            }
            log.error("Error on closing statement : " + ExceptionUtils.getMessage(ex));
        }
    }

    private void closeStatement(Statement statement) {
        try {
            if (statement == null || statement.isClosed()) {
                log.error("Statement is null or already closde.");
                return;
            }
            statement.close();
        } catch (Throwable ex) {
            if (log.isDebugEnabled()) {
                log.debug(ExceptionUtils.getMessage(ex));
            }
            log.error("Error on closing statement : " + ExceptionUtils.getMessage(ex));
        }
    }

    public void insertDataOfAuthorityFile(String user_id, String number, String api_key, String api_secret) {
        PreparedStatement statement = null;
        try {
            Connection _connection = this.getConnection();
            String query = "INSERT INTO authority"
                    + "(api_key,api_secret,number,user_id,get_message,sent_message) VALUES"
                    + "(?,?,?,?,?,?)";
            statement = _connection.prepareStatement(query);
            statement.setString(1, api_key);
            statement.setString(2, api_secret);
            statement.setString(3, number);
            statement.setString(4, user_id);
            statement.setInt(5, 0);
            statement.setInt(6, 0);
            statement.executeUpdate();

        } catch (Throwable ex) {
            if (log.isDebugEnabled()) {
                log.debug(ExceptionUtils.getMessage(ex));
            }
            log.error(ExceptionUtils.getMessage(ex));
        } finally {
            this.closePreparedStatement(statement);
        }
    }

    public void updateNotSentMessageStatus(String from, String to, String status_code) {
        PreparedStatement statement = null;
        try {
            Connection _connection = this.getConnection();
            statement = _connection.prepareStatement("update inbound_message set message_status=?,status_code=? where message_from=? and message_to=? ;");
            statement.setInt(1, 2);
            statement.setString(2, status_code);
            statement.setString(3, from);
            statement.setString(4, to);
            statement.executeUpdate();
        } catch (Throwable ex) {
            if (log.isDebugEnabled()) {
                log.debug(ExceptionUtils.getMessage(ex));
            }
            log.error(ExceptionUtils.getMessage(ex));
        } finally {
            this.closePreparedStatement(statement);
        }
    }

    public void updateMessageStatus(String from, long botSendingTime, String to, String status_code) {
        PreparedStatement statement = null;
        try {
            Connection _connection = this.getConnection();
            statement = _connection.prepareStatement("update inbound_message set message_status=?,last_send_time=?,status_code=? where message_from=? and message_to=? ;");
            statement.setInt(1, 1);
            statement.setLong(2, botSendingTime);
            statement.setString(3, status_code);
            statement.setString(4, from);
            statement.setString(5, to);
            statement.executeUpdate();
        } catch (Throwable ex) {
            if (log.isDebugEnabled()) {
                log.debug(ExceptionUtils.getMessage(ex));
            }
            log.error(ExceptionUtils.getMessage(ex));
        } finally {
            this.closePreparedStatement(statement);
        }
    }

    public List<AuthInfo> getAuthorityInfoFromDb(String user_id) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            Connection _connection = this.getConnection();
            List<AuthInfo> authInfoModels = new ArrayList<AuthInfo>();
            statement = _connection.createStatement();
            rs = statement.executeQuery("select * from authority where user_id=" + user_id + ";");
            while (rs.next()) {
                AuthInfo aModel = new AuthInfo();
                aModel.setId("" + rs.getInt("id"));
                aModel.setApiKey(rs.getString("api_key"));
                aModel.setApiSecret(rs.getString("api_secret"));
                aModel.setNumber(rs.getString("number"));
                aModel.setUser_id(rs.getString("user_id"));
                aModel.setGetMessage(rs.getInt("get_message"));
                aModel.setSentMessage(rs.getInt("sent_message"));
                authInfoModels.add(aModel);

            }
            return authInfoModels;
        } catch (Throwable ex) {
            if (log.isDebugEnabled()) {
                log.debug(ExceptionUtils.getMessage(ex));
            }
            log.error(ExceptionUtils.getMessage(ex));
        } finally {
            this.closeStatement(statement);
        }
        return null;
    }

    public void deleteAuthInfoFromDB(List<Integer> list) {
        PreparedStatement statement = null;
        for (Integer in : list) {
            try {
                Connection _connection = this.getConnection();
                statement = _connection.prepareStatement("delete from authority where id=?;");
                statement.setInt(1, in);
                statement.executeUpdate();
            } catch (Throwable ex) {
                if (log.isDebugEnabled()) {
                    log.debug(ExceptionUtils.getMessage(ex));
                }
                log.error(ExceptionUtils.getMessage(ex));
            } finally {
                this.closePreparedStatement(statement);
            }
        }
    }

    public void UpdateAuthInfoGetMessage(AuthInfo authInfo) {
        PreparedStatement statement = null;
        try {
            Connection _connection = this.getConnection();
            statement = _connection.prepareStatement("update authority set get_message=? where number=?;");
            statement.setInt(1, authInfo.getGetMessage() + 1);
            statement.setString(2, authInfo.getNumber());
            statement.executeUpdate();
        } catch (Throwable ex) {
            if (log.isDebugEnabled()) {
                log.debug(ExceptionUtils.getMessage(ex));
            }
            log.error(ExceptionUtils.getMessage(ex));
        } finally {
            this.closePreparedStatement(statement);
        }
    }

    public void UpdateAuthInfoSentMessage(AuthInfo authInfo) {
        PreparedStatement statement = null;
        try {
            Connection _connection = this.getConnection();
            statement = _connection.prepareStatement("update authority set sent_message=? where number=?;");
            statement.setInt(1, authInfo.getSentMessage() + 1);
            statement.setString(2, authInfo.getNumber());
            statement.executeUpdate();
        } catch (Throwable ex) {
            if (log.isDebugEnabled()) {
                log.debug(ExceptionUtils.getMessage(ex));
            }
            log.error(ExceptionUtils.getMessage(ex));
        } finally {
            this.closePreparedStatement(statement);
        }
    }

    public UserInfo getUserInfoFromDb(String username, String password) {
        UserInfo userInfo = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            Connection _connection = this.getConnection();
            statement = _connection.createStatement();
            rs = statement.executeQuery("select * from users where username='" + username + "' and password='" + password + "';");
            if (rs.next()) {
                userInfo = new UserInfo();
                userInfo.setId("" + rs.getInt("id"));
                userInfo.setUsername(rs.getString("username"));
                userInfo.setPassword(rs.getString("password"));
                userInfo.setType(rs.getString("type"));
                userInfo.setStatus(rs.getString("status"));
                //_connection.close();
                return userInfo;
            } else {
                return userInfo;

            }

        } catch (Throwable ex) {
            if (log.isDebugEnabled()) {
                log.debug(ExceptionUtils.getMessage(ex));
            }
            log.error(ExceptionUtils.getMessage(ex));
        } finally {
            this.closeStatement(statement);
        }
        return userInfo;

    }

    public void updateDataForMsgStatusOne(Inbound inb) {
        PreparedStatement statement = null;
        try {
            Connection _connection = this.getConnection();
            statement = _connection.prepareStatement("UPDATE inbound_message "
                    + "SET message_status=?,send_count=?,next_send_time=?,date_recieved=?,body=?,recieved_time=?,last_send_time=? WHERE message_from=? and message_to=?");
            statement.setInt(1, inb.getMessage_status());
            statement.setInt(2, inb.getSend_count());
            statement.setLong(3, inb.getNext_send_time());
            statement.setString(4, inb.getDate_recieved());
            statement.setString(5, inb.getBody());
            statement.setLong(6, inb.getRecieved_time());
            statement.setLong(7, 0);
            statement.setString(8, inb.getMessage_from());
            statement.setString(9, inb.getMessage_to());
            statement.executeUpdate();
        } catch (Throwable ex) {
            if (log.isDebugEnabled()) {
                log.debug(ExceptionUtils.getMessage(ex));
            }
            log.error(ExceptionUtils.getMessage(ex));
        } finally {
            this.closePreparedStatement(statement);
        }

    }

    public List<Inbound> getDataByNumberUsingStatus(String user_id) throws SQLException {
        Statement statement = null;
        ResultSet rs = null;
        Inbound in = null;
        List<Inbound> inboundMessages = new ArrayList<Inbound>();
        try {
            Connection _connection = this.getConnection();
            statement = _connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM inbound_message where message_status=0 and user_id="+user_id+";");
            while (rs.next()) {
                in = new Inbound();
                in.setId(rs.getInt("id"));
                in.setAccount_id(rs.getString("account_id"));
                in.setBody(rs.getString("body"));
                in.setCreation_date(rs.getString("creation_date"));
                in.setDate_recieved(rs.getString("date_recieved"));
                in.setNext_send_time(Long.parseLong(rs.getString("next_send_time")));
                in.setMessage_from(rs.getString("message_from"));
                in.setMessage_id(rs.getString("message_id"));
                in.setMessage_status(rs.getInt("message_status"));
                in.setMessage_to(rs.getString("message_to"));
                in.setMessage_type(rs.getString("message_type"));
                in.setNetwork(rs.getString("network"));
                in.setApi_key(rs.getString("api_key"));
                in.setApi_secret(rs.getString("api_secret"));
                in.setRecieved_time(Long.parseLong(rs.getString("recieved_time")));
                in.setSend_count(rs.getInt("send_count"));
                in.setUser_id(rs.getString("user_id"));
                in.setStatus_code(rs.getString("status_code"));
                in.setLast_send_time(Long.parseLong(rs.getString("last_send_time")));
                //conn.close();
                inboundMessages.add(in);

            }
            return inboundMessages;
        } catch (Throwable ex) {
            if (log.isDebugEnabled()) {
                log.debug(ExceptionUtils.getMessage(ex));
            }
            log.error(ExceptionUtils.getMessage(ex));
        } finally {
            this.closeStatement(statement);
        }
        return inboundMessages;

    }
    public Inbound isExistingData(String fromNumber, String toNumber, String user_id) {
        Statement statement = null;
        ResultSet rs = null;
        Inbound in = null;
        try {
            Connection _connection = this.getConnection();
            statement = _connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM inbound_message where message_from=" + fromNumber + " and message_to=" + toNumber + " and user_id=" + user_id + ";");
            if (rs.next()) {
                in = new Inbound();
                in.setId(rs.getInt("id"));
                in.setAccount_id(rs.getString("account_id"));
                in.setBody(rs.getString("body"));
                in.setCreation_date(rs.getString("creation_date"));
                in.setDate_recieved(rs.getString("date_recieved"));
                in.setNext_send_time(Long.parseLong(rs.getString("next_send_time")));
                in.setMessage_from(rs.getString("message_from"));
                in.setMessage_id(rs.getString("message_id"));
                in.setMessage_status(rs.getInt("message_status"));
                in.setMessage_to(rs.getString("message_to"));
                in.setMessage_type(rs.getString("message_type"));
                in.setNetwork(rs.getString("network"));
                in.setRecieved_time(Long.parseLong(rs.getString("recieved_time")));
                in.setSend_count(rs.getInt("send_count"));
                in.setUser_id(rs.getString("user_id"));
                in.setStatus_code(rs.getString("status_code"));
                in.setLast_send_time(Long.parseLong(rs.getString("last_send_time")));
                //_connection.close();
                return in;
            } else {
                return in;

            }

        } catch (Throwable ex) {
            if (log.isDebugEnabled()) {
                log.debug(ExceptionUtils.getMessage(ex));
            }
            log.error(ExceptionUtils.getMessage(ex));
        } finally {
            this.closeStatement(statement);
        }
        return in;
    }

    public boolean isMessageStatusOne(String from) {

        Statement statement = null;
        ResultSet rs = null;
        boolean flag = false;
        try {
            Connection _connection = this.getConnection();
            statement = _connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM inbound_message where message_from=" + from + ";");
            while (rs.next()) {
                if (rs.getInt("message_status") == 1) {
                    flag = true;
                }
            }
        } catch (Throwable ex) {
            if (log.isDebugEnabled()) {
                log.debug(ExceptionUtils.getMessage(ex));
            }
            log.error(ExceptionUtils.getMessage(ex));
        } finally {
            this.closeStatement(statement);
        }
        return flag;
    }

    public static void main(String args[]) {

    }
}
