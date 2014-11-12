package edu.cmu.sv.ws.ssnoc.rest;

import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;
import edu.cmu.sv.ws.ssnoc.dto.Message;
import edu.cmu.sv.ws.ssnoc.dto.User;
import junit.framework.TestCase;
import org.junit.AfterClass;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageServiceTest extends TestCase {

    @Override
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

    @Override
    public void tearDown() throws Exception {
        File file = new File(System.getProperty("user.home") + "/h2db.mv.db");
        File file2 = new File(System.getProperty("user.home") + "/h2db.mv.db.backup");

        file.delete();
        file2.renameTo(file);
    }

    public void testPostOnWall() throws Exception {

        DBUtils.initializeDatabase();

        UserService us = new UserService();
        User u = new User();
        u.setUserName("demo");
        u.setPassword("asdf");

        us.addUser(u);

        MessageService ms = new MessageService();
        Message message = new Message();
        message.setContent("test");
        ms.postOnWall("demo", message);

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

    public void testBadPostOnWall() throws Exception {

        DBUtils.initializeDatabase();

        MessageService ms = new MessageService();
        Message message = new Message();
        message.setContent("test");
        ms.postOnWall("nobody", message);

        MessagesService mss = new MessagesService();
        List<Message> ret = mss.getWallPosts();
        List<String> authors = new ArrayList<String>();
        for (Message m : ret) {
            authors.add(m.getAuthor());
        }
        boolean passed = false;
        for (String author : authors) {
            if (author.equals("nobody")) {
                passed = true;
                break;
            }
        }
        assertFalse(passed);
    }

    public void testPostOnWallHasRightAuthor() throws Exception {

        DBUtils.initializeDatabase();

        UserService us = new UserService();
        User u = new User();
        u.setUserName("demo");
        u.setPassword("asdf");

        us.addUser(u);

        MessageService ms = new MessageService();
        Message message = new Message();
        message.setContent("test");
        ms.postOnWall("demo", message);

        MessagesService mss = new MessagesService();
        List<Message> ret = mss.getWallPosts();
        List<String> contents = new ArrayList<String>();
        for (Message m : ret) {
            contents.add(m.getAuthor());
        }
        boolean passed = false;
        for (String content : contents) {
            if (content.equals("demo")) {
                passed = true;
                break;
            }
        }
        assertTrue(passed);
    }

    public void testPostOnWallMultiple() throws Exception {

        DBUtils.initializeDatabase();

        UserService us = new UserService();
        User u = new User();
        u.setUserName("demo");
        u.setPassword("asdf");

        us.addUser(u);

        MessageService ms = new MessageService();
        Message message = new Message();
        message.setContent("test");
        ms.postOnWall("demo", message);
        Message message2 = new Message();
        message2.setContent("test2");
        ms.postOnWall("demo", message2);

        MessagesService mss = new MessagesService();
        List<Message> ret = mss.getWallPosts();
        List<String> contents = new ArrayList<String>();
        for (Message m : ret) {
            contents.add(m.getContent());
        }
        boolean passed = false;
        boolean passed2 = false;
        for (String content : contents) {
            if (content.equals("test")) {
                passed = true;
            }
            if (content.equals("test2")) {
                passed2 = true;
            }
        }
        assertTrue(passed && passed2);
    }

    public void testSendPM() throws Exception {

        DBUtils.initializeDatabase();

        UserService us = new UserService();
        User u = new User();
        u.setUserName("jc");
        u.setPassword("asdf");
        User u2 = new User();
        u2.setUserName("vinay");
        u2.setPassword("asdf");
        us.addUser(u);
        us.addUser(u2);

        MessageService ms = new MessageService();
        Message message = new Message();
        message.setContent("test");
        ms.sendPM("vinay", "jc", message);

        MessagesService mss = new MessagesService();
        List<Message> ret = mss.getPMs("vinay", "jc");
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


    public void testSendBadPM() throws Exception {

        DBUtils.initializeDatabase();

        MessageService ms = new MessageService();
        Message message = new Message();
        message.setContent("test");
        ms.sendPM("nobody", "nobody2", message);

        MessagesService mss = new MessagesService();
        List<Message> ret = mss.getPMs("nobody", "nobody2");
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
        assertFalse(passed);
    }


    public void testGetPM() throws Exception {
        DBUtils.initializeDatabase();

        MessageService ms = new MessageService();
        String res = ms.getPMs("-1");
        assertEquals(res, null);
    }


    public void testSendPMHasRightAuthor() throws Exception {

        DBUtils.initializeDatabase();

        UserService us = new UserService();
        User u = new User();
        u.setUserName("jc");
        u.setPassword("asdf");
        User u2 = new User();
        u2.setUserName("vinay");
        u2.setPassword("asdf");

        MessageService ms = new MessageService();
        Message message = new Message();
        message.setContent("test");
        ms.sendPM("vinay", "jc", message);

        MessagesService mss = new MessagesService();
        List<Message> ret = mss.getPMs("vinay", "jc");
        List<String> contents = new ArrayList<String>();
        for (Message m : ret) {
            contents.add(m.getAuthor());
        }
        boolean passed = false;
        for (String content : contents) {
            if (content.equals("vinay")) {
                passed = true;
                break;
            }
        }
        assertTrue(passed);
    }


    public void testSendPMHasRightTarget() throws Exception {

        DBUtils.initializeDatabase();

        UserService us = new UserService();
        User u = new User();
        u.setUserName("jc");
        u.setPassword("asdf");
        User u2 = new User();
        u2.setUserName("vinay");
        u2.setPassword("asdf");
        us.addUser(u);
        us.addUser(u2);

        MessageService ms = new MessageService();
        Message message = new Message();
        message.setContent("test");
        ms.sendPM("vinay", "jc", message);

        MessagesService mss = new MessagesService();
        List<Message> ret = mss.getPMs("vinay", "jc");
        List<String> contents = new ArrayList<String>();
        for (Message m : ret) {
            contents.add(m.getTarget());
        }
        boolean passed = false;
        for (String content : contents) {
            if (content.equals("jc")) {
                passed = true;
                break;
            }
        }
        assertTrue(passed);
    }

    public void testSendPMBackAndForth() throws Exception {

        DBUtils.initializeDatabase();

        UserService us = new UserService();
        User u = new User();
        u.setUserName("jc");
        u.setPassword("asdf");
        User u2 = new User();
        u2.setUserName("vinay");
        u2.setPassword("asdf");
        us.addUser(u);
        us.addUser(u2);

        MessageService ms = new MessageService();
        Message message = new Message();
        message.setContent("test");
        ms.sendPM("vinay", "jc", message);
        Message message2 = new Message();
        message2.setContent("test2");
        ms.sendPM("jc", "vinay", message2);

        MessagesService mss = new MessagesService();
        List<Message> ret = mss.getPMs("vinay", "jc");
        List<String> contents = new ArrayList<String>();
        for (Message m : ret) {
            contents.add(m.getContent());
        }
        boolean passed = false;
        boolean passed2 = false;
        for (String content : contents) {
            if (content.equals("test")) {
                passed = true;
            }
        }
        for (String content : contents) {
            if (content.equals("test2")) {
                passed2 = true;
            }
        }
        assertTrue(passed && passed2);
    }


    @AfterClass
    public void testSocialNetworkWithNobody() throws Exception {

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

        UsersService users = new UsersService();
        String result = "[[]]";
        assertEquals(users.getClusters(("1")), result);
    }

    @AfterClass
    public void testSocialNetworkNoTime() throws Exception {


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
        u3.setUserName("gaya");
        u3.setPassword("testing");

        us.addUser(u);
        us.addUser(u2);
        us.addUser(u3);

        MessageService ms = new MessageService();
        Message message = new Message();
        message.setContent("test");
        ms.sendPM("vinay", "jc", message);

        UsersService users = new UsersService();
        String res = users.getClusters(("0"));
        assertTrue(res.indexOf("jc") == res.lastIndexOf("jc"));
        assertTrue(res.indexOf("vinay") == res.lastIndexOf("vinay"));
        assertTrue(res.indexOf("gaya") == res.lastIndexOf("gaya"));
    }

    @AfterClass
    public void testSocialNetwork() throws Exception {


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
        u3.setUserName("gaya");
        u3.setPassword("testing");

        us.addUser(u);
        us.addUser(u2);
        us.addUser(u3);

        MessageService ms = new MessageService();
        Message message = new Message();
        message.setContent("test");
        ms.sendPM("vinay", "jc", message);

        UsersService users = new UsersService();
        String res = users.getClusters(("1"));
        assertTrue(res.indexOf("gaya") != res.lastIndexOf("gaya"));
        assertFalse(res.indexOf("jc") == -1);
        assertFalse(res.indexOf("vinay") == -1);
    }

    @AfterClass
    public void testSocialNetworkThreeCluster() throws Exception {


        DBUtils.initializeDatabase();

        String query = "delete from PRIVATE_MESSAGES";
        String que = "delete from SSN_MESSAGES";
        String query1 = "delete from SSN_USERS";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.execute();
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(que);) {
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
        u3.setUserName("gaya");
        u3.setPassword("testing");

        us.addUser(u);
        us.addUser(u2);
        us.addUser(u3);


        UsersService users = new UsersService();
        users.getClusters("1");
        String res = users.getClusters(("1"));
        assertFalse(res.indexOf("jc") == -1);
        assertFalse(res.indexOf("vinay") == -1);
        assertFalse(res.indexOf("gaya") == -1);
    }

    protected Connection getConnection() throws SQLException {
        return DBUtils.getConnection();
    }

}