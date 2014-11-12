package edu.cmu.sv.ws.ssnoc.data.dao;

import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;
import edu.cmu.sv.ws.ssnoc.dto.Message;
import edu.cmu.sv.ws.ssnoc.dto.User;
import edu.cmu.sv.ws.ssnoc.rest.MessagesService;
import edu.cmu.sv.ws.ssnoc.rest.UserService;
import junit.framework.TestCase;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MessageDAOImplTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
        File file = new File(System.getProperty("user.home") + "/h2db.mv.db");
        File file2 = new File(System.getProperty("user.home") + "/h2db.mv.db.backup");

        if (!file2.exists()) {
            file.renameTo(file2);
        } else {
            file2.delete();
            file.renameTo(file2);
        }
    }

    public void tearDown() throws Exception {
        File file = new File(System.getProperty("user.home") + "/h2db.mv.db");
        File file2 = new File(System.getProperty("user.home") + "/h2db.mv.db.backup");

        file.delete();
        file2.renameTo(file);
    }

    public void testCantSaveNullWallPost() throws Exception {
        DBUtils.initializeDatabase();

        UserService us = new UserService();
        User u = new User();
        u.setUserName("demo");
        u.setPassword("asdf");
        us.addUser(u);

        assertFalse(DAOFactory.getInstance().getMessageDAO().save(null));

        MessagesService mss = new MessagesService();
        List<Message> ret = mss.getWallPosts();

        assertEquals(ret.size(), 0);
    }

    public void testCanSavePubicMessage() throws Exception {
        DBUtils.initializeDatabase();

        UserService us = new UserService();
        User u = new User();
        u.setUserName("demo");
        u.setPassword("asdf");
        us.addUser(u);

        Message message = new Message();
        message.setContent("test");
        message.setAuthor("demo");
        message.setPublic(true);
        message.setTimestamp("1970-01-01 12:00:00");

        assertTrue(DAOFactory.getInstance().getMessageDAO().save(ConverterUtils.convert(message)));

        MessagesService mss = new MessagesService();
        List<Message> ret = mss.getWallPosts();
        List<String> contents = new ArrayList<String>();
        for (Message m : ret) {
            contents.add(m.getContent());
        }
        boolean passed = false;
        for (String content : contents) {
            if (content.equals("test")) {
                passed = true;
                break;
            }
        }
        assertTrue(passed);
    }

    public void testCanSavePM() throws Exception {
        DBUtils.initializeDatabase();

        UserService us = new UserService();
        User u = new User();
        u.setUserName("jc");
        u.setPassword("asdf");
        User u2 = new User();
        u2.setUserName("vinay");
        u2.setPassword("asdf");
        us.addUser(u);

        Message message = new Message();
        message.setContent("test");
        message.setAuthor("jc");
        message.setTarget("vinay");
        message.setPublic(false);
        message.setTimestamp("1970-01-01 12:00:00");

        assertTrue(DAOFactory.getInstance().getMessageDAO().save(ConverterUtils.convert(message)));

        MessagesService mss = new MessagesService();
        List<Message> ret = mss.getPMs("jc", "vinay");
        List<String> contents = new ArrayList<String>();
        for (Message m : ret) {
            contents.add(m.getContent());
        }
        boolean passed = false;
        for (String content : contents) {
            if (content.equals("test")) {
                passed = true;
                break;
            }
        }
        assertTrue(passed);
    }

    public void testCantSaveNullPMs() throws Exception {
        DBUtils.initializeDatabase();

        assertFalse(DAOFactory.getInstance().getMessageDAO().save(null));

        MessagesService mss = new MessagesService();
        List<Message> ret = mss.getPMs("", "");

        assertEquals(ret.size(), 0);
    }

    public void testCantSendPMFromNonexistentUser() throws Exception {
        DBUtils.initializeDatabase();

        Message msg = new Message();
        msg.setPublic(false);
        msg.setAuthor("nobody");

        assertFalse(DAOFactory.getInstance().getMessageDAO().save(ConverterUtils.convert(msg)));

        MessagesService mss = new MessagesService();
        List<Message> ret = mss.getPMs("nobody", "");

        assertEquals(ret.size(), 0);
    }

    public void testCantPostToWallFromNonexistentUser() throws Exception {
        DBUtils.initializeDatabase();

        Message msg = new Message();
        msg.setPublic(true);
        msg.setAuthor("nobody");

        assertFalse(DAOFactory.getInstance().getMessageDAO().save(ConverterUtils.convert(msg)));

        MessagesService mss = new MessagesService();
        List<Message> ret = mss.getWallPosts();

        assertEquals(ret.size(), 0);
    }

    public void testAnalyzeWithNobodyReturnsNobody() throws Exception {
        DBUtils.initializeDatabase();

        String query = "delete from PRIVATE_MESSAGES";
        String query2 = "delete from SSN_MESSAGES";
        String query1 = "delete from SSN_USERS";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.execute();
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query2);) {
            stmt.execute();
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query1);) {
            stmt.execute();
        }

        List<List<UserPO>> ret = DAOFactory.getInstance().getMessageDAO().getClusters(Timestamp.valueOf("1970-01-01 12:00:00"));
        assertEquals(ret.size(), 1);
        assertEquals(ret.get(0).size(), 0);
    }

    public void testAnalyzeWithTwoTalkers() throws Exception {
        DBUtils.initializeDatabase();

        String query = "delete from PRIVATE_MESSAGES";
        String query2 = "delete from SSN_MESSAGES";
        String query1 = "delete from SSN_USERS";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.execute();
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query2);) {
            stmt.execute();
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query1);) {
            stmt.execute();
        }

        UserService us = new UserService();
        User u = new User();
        u.setUserName("jc");
        u.setPassword("asdf");
        User u2 = new User();
        u2.setUserName("vinay");
        u2.setPassword("asdf");
        us.addUser(u);
        us.addUser(u2);

        Message message = new Message();
        message.setContent("test");
        message.setAuthor("jc");
        message.setTarget("vinay");
        message.setPublic(false);
        message.setTimestamp("2000-01-01 12:00:00");

        DAOFactory.getInstance().getMessageDAO().save(ConverterUtils.convert(message));

        List<List<UserPO>> ret = DAOFactory.getInstance().getMessageDAO().getClusters(Timestamp.valueOf("1970-01-01 12:00:00"));
        assertEquals(ret.size(), 2);
        assertEquals(ret.get(0).size(), 1);
        assertEquals(ret.get(1).size(), 1);
        assertFalse(ret.get(0).get(0).getUserName() == ret.get(1).get(0).getUserName());
    }

    public void testAnalyzeWithTwoSilentPeople() throws Exception {
        DBUtils.initializeDatabase();

        String query = "delete from PRIVATE_MESSAGES";
        String query2 = "delete from SSN_MESSAGES";
        String query1 = "delete from SSN_USERS";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.execute();
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query2);) {
            stmt.execute();
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query1);) {
            stmt.execute();
        }

        UserService us = new UserService();
        User u = new User();
        u.setUserName("jc");
        u.setPassword("asdf");
        User u2 = new User();
        u2.setUserName("vinay");
        u2.setPassword("asdf");
        us.addUser(u);
        us.addUser(u2);

        List<List<UserPO>> ret = DAOFactory.getInstance().getMessageDAO().getClusters(Timestamp.valueOf("1970-01-01 12:00:00"));
        assertEquals(ret.size(), 1);
        assertEquals(ret.get(0).size(), 2);
        assertFalse(ret.get(0).get(1).getUserName() == ret.get(0).get(0).getUserName());
    }

    public void testAnalyzeThrowsAwayIdenticalSubArrays() throws Exception {
        DBUtils.initializeDatabase();

        String query = "delete from PRIVATE_MESSAGES";
        String query2 = "delete from SSN_MESSAGES";
        String query1 = "delete from SSN_USERS";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.execute();
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query2);) {
            stmt.execute();
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query1);) {
            stmt.execute();
        }

        UserService us = new UserService();
        User u = new User();
        u.setUserName("jc");
        u.setPassword("asdf");
        User u2 = new User();
        u2.setUserName("vinay");
        u2.setPassword("asdf");
        User u3 = new User();
        u3.setUserName("inbetween");
        u3.setPassword("asdf");
        us.addUser(u);
        us.addUser(u2);
        us.addUser(u3);

        Message message = new Message();
        message.setContent("test");
        message.setAuthor("jc");
        message.setTarget("inbetween");
        message.setPublic(false);
        message.setTimestamp("2000-01-01 12:00:00");

        Message message2 = new Message();
        message2.setContent("test");
        message2.setAuthor("inbetween");
        message2.setTarget("vinay");
        message2.setPublic(false);
        message2.setTimestamp("2000-01-01 12:00:01");

        DAOFactory.getInstance().getMessageDAO().save(ConverterUtils.convert(message));
        DAOFactory.getInstance().getMessageDAO().save(ConverterUtils.convert(message2));

        List<List<UserPO>> ret = DAOFactory.getInstance().getMessageDAO().getClusters(Timestamp.valueOf("1970-01-01 12:00:00"));
        assertEquals(ret.size(), 2);
        assertEquals(ret.get(0).size(), 1);
        assertEquals(ret.get(1).size(), 2);
    }

    protected Connection getConnection() throws SQLException {
        return DBUtils.getConnection();
    }
}