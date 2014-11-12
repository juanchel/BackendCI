package edu.cmu.sv.ws.ssnoc.data.dao;

import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;
import edu.cmu.sv.ws.ssnoc.dto.Message;
import edu.cmu.sv.ws.ssnoc.dto.User;
import edu.cmu.sv.ws.ssnoc.rest.MessagesService;
import edu.cmu.sv.ws.ssnoc.rest.UserService;
import junit.framework.TestCase;

import java.io.File;
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

        DAOFactory.getInstance().getMessageDAO().save(null);

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

        DAOFactory.getInstance().getMessageDAO().save(ConverterUtils.convert(message));

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

        DAOFactory.getInstance().getMessageDAO().save(ConverterUtils.convert(message));

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

}