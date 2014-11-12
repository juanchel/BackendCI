package edu.cmu.sv.ws.ssnoc.data.dao;

import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.TestResult;

/**
 * DAO implementation for saving User information in the H2 database.
 */
public class MessageDAOImpl extends BaseDAOImpl implements IMessageDAO {
    /**
     * This method will save the information of the user into the database.
     *
     * @param messagePO - User information to be saved.
     */
    public void save(MessagePO messagePO) {
        Log.enter(messagePO);
        if (messagePO == null) {
            Log.warn("Inside save method with messagePO == NULL");
            return;
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.DOES_USER_EXIST)) {
            stmt.setString(1, messagePO.getAuthor());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            if (rs.getInt(1) == 0) {
                return;
            }
        } catch (SQLException e) {
            handleException(e);
        }

        if (messagePO.getPublic()) {
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SQL.POST_ON_WALL)) {
                stmt.setString(1, messagePO.getContent());
                stmt.setString(2, messagePO.getAuthor());
                stmt.setTimestamp(3, Timestamp.valueOf(messagePO.getTimestamp()));
                int rowCount = stmt.executeUpdate();
                Log.trace("Statement executed, and " + rowCount + " rows inserted.");
            } catch (SQLException e) {
                handleException(e);
            } finally {
                Log.exit();
            }
        } else {
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SQL.SEND_PRIVATE_MESSAGE)) {
                stmt.setString(1, messagePO.getContent());
                stmt.setString(2, messagePO.getAuthor());
                stmt.setString(3, messagePO.getTarget());
                stmt.setString(4, messagePO.getTimestamp());
                int rowCount = stmt.executeUpdate();
                Log.trace("Statement executed, and " + rowCount + " rows inserted.");
            } catch (SQLException e) {
                handleException(e);
            } finally {
                Log.exit();
            }
        }

    }

    public void saveAnnouncement(MessagePO messagePO) {
        Log.enter(messagePO);
        if (messagePO == null) {
            Log.warn("Inside save method with messagePO == NULL");
            return;
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.POST_ANNOUNCEMENT)) {
            stmt.setString(1, messagePO.getContent());
            stmt.setString(2, messagePO.getAuthor());
            stmt.setTimestamp(3, Timestamp.valueOf(messagePO.getTimestamp()));
            int rowCount = stmt.executeUpdate();
            Log.trace("Statement executed, and " + rowCount + " rows inserted.");
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit();
        }
    }

    public List<MessagePO> loadAnnouncement() {
        Log.enter();

        String query = SQL.GET_ALL_ANNOUNCEMENTS;

        List<MessagePO> messages = new ArrayList<MessagePO>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            messages = processPublicResults(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(messages);
        }
        return messages;
    }

    public void testSave(MessagePO messagePO) {
        Log.enter(messagePO);
        if (messagePO == null) {
            Log.warn("Inside save method with messagePO == NULL");
            return;
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.TEST_POST_ON_WALL)) {
            stmt.setString(1, messagePO.getContent());
            stmt.setString(2, messagePO.getAuthor());
            stmt.setTimestamp(3, Timestamp.valueOf(messagePO.getTimestamp()));
            int rowCount = stmt.executeUpdate();
            Log.trace("Statement executed, and " + rowCount + " rows inserted.");
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit();
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn
                     .prepareStatement(SQL.TEST_COUNT_POST)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            handleException(e);
        }

    }


    /**
     * This method will load all the users in the
     * database.
     *
     * @return - List of messages.
     */
    public List<MessagePO> loadWallMessages() {
        Log.enter();

        String query = SQL.GET_ALL_PUBLIC_MESSAGES;

        List<MessagePO> messages = new ArrayList<MessagePO>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            messages = processPublicResults(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(messages);
        }
        return messages;
    }

    public List<MessagePO> testLoadWallMessages() {
        Log.enter();

        String query = SQL.TEST_GET_FROM_WALL;

        List<MessagePO> messages = new ArrayList<MessagePO>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            messages = processPublicResults(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(messages);
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn
                     .prepareStatement(SQL.TEST_COUNT_GET)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            handleException(e);
        }

        return messages;
    }

    public List<List<UserPO>> getClusters(Timestamp timestamp) {
        Log.enter();

        String query = SQL.FIND_ALL_USERNAMES;

        List<String> usernames = new ArrayList<String>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                usernames.add(rs.getString(1));
            }
        } catch (SQLException e) {
            handleException(e);
        }

        String query2 = SQL.FIND_TALKERS_BY_TIME;

        List<String> authors = new ArrayList<String>();
        List<String> targets = new ArrayList<String>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query2);) {
            stmt.setTimestamp(1, timestamp);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                authors.add(rs.getString(1));
                targets.add(rs.getString(2));
            }
        } catch (SQLException e) {
            handleException(e);
        }

        Set<String> initSet = new HashSet<String>();
        Set<String> losers = new HashSet<String>();

        for (String a : authors) {
            initSet.add(a);
        }
        for (String t : targets) {
            initSet.add(t);
        }

        losers = new HashSet<String>(usernames);
        losers.removeAll(initSet);

        Set<Set<String>> clusterSet = new HashSet<Set<String>>();
        clusterSet.add(initSet);

        for (int i = 0; i < authors.size(); i++) {

            System.out.println("AUTHOR " + authors.get(i) + "  TARGET " + targets.get(i));

            Set<Set<String>> toRemove = new HashSet<Set<String>>();
            Set<Set<String>> toAdd = new HashSet<Set<String>>();
            Set<String> toAdd1 = new HashSet<String>();
            Set<String> toAdd2 = new HashSet<String>();

            for (Set<String> group : clusterSet) {
                if (group.contains(authors.get(i)) && group.contains(targets.get(i))) {
                    toRemove.add(group);
                    toAdd1 = new HashSet<String>(group);
                    toAdd1.remove(authors.get(i));
                    toAdd2 = new HashSet<String>(group);
                    toAdd2.remove(targets.get(i));
                    toAdd.add(toAdd1);
                    toAdd.add(toAdd2);
                }
            }
            clusterSet.removeAll(toRemove);
            clusterSet.addAll(toAdd);

            Set<Set<String>> toRemove2 = new HashSet<Set<String>>();
            for (Set<String> group : clusterSet) {
                for (Set<String> group2 : clusterSet) {
                    if (group.containsAll(group2) && !group.equals(group2)) {
                        toRemove2.add(group2);
                    }
                }
            }

            clusterSet.removeAll(toRemove2);

//            Iterator<Set<String>> iterator = clusterSet.iterator();
//
//            while (iterator.hasNext()) {
//                Set<String> nextGroup = iterator.next();
//                Set<String> secondGroup =  new HashSet<String>(nextGroup);
//                System.out.println(nextGroup);
//
//                if (nextGroup.contains(authors.get(i)) && nextGroup.contains(targets.get(i))) {
//                    clusterSet.remove(nextGroup);
//                    nextGroup.remove(authors.get(i));
//                    clusterSet.add(nextGroup);
//                    secondGroup.remove(targets.get(i));
//                    clusterSet.add(secondGroup);
//                }
//            }
        }

        for (Set<String> group : clusterSet) {
            group.addAll(losers);
        }

        // Conversion from set set to list list
        List<List<UserPO>> clusters = new ArrayList<List<UserPO>>();
        for (Set<String> group : clusterSet) {
            List<UserPO> poList = new ArrayList<UserPO>();
            for (String un : group) {
                UserPO po = new UserPO();
                po.setUserName(un);
                poList.add(po);
            }
            clusters.add(poList);

        }


        return clusters;
    }

    /**
     * This method will load all the users in the
     * database.
     *
     * @return - List of messages.
     */
    public List<MessagePO> loadPrivateMessages(String author, String target) {

        List<MessagePO> po = null;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn
                     .prepareStatement(SQL.GET_PM_BY_USER_ID)) {
            stmt.setString(1, author);
            stmt.setString(2, target);
            stmt.setString(3, target);
            stmt.setString(4, author);

            po = processPrivateResults(stmt);
        } catch (SQLException e) {
            handleException(e);
        }

        return po;
    }

    public boolean testCheckTime() {

        boolean valid = true;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn
                     .prepareStatement(SQL.TEST_CHECK_TIME)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                valid = rs.getBoolean(1);
            }
        } catch (SQLException e) {
            handleException(e);
        }

        return valid;
    }

    private List<MessagePO> processPublicResults(PreparedStatement stmt) {
        Log.enter(stmt);

        if (stmt == null) {
            Log.warn("Inside processMessageById method with NULL statement object.");
            return null;
        }

        Log.debug("Executing stmt = " + stmt);
        List<MessagePO> messages = new ArrayList<MessagePO>();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                MessagePO po = new MessagePO();
                po = new MessagePO();
                po.setContent(rs.getString(2));
                po.setAuthor(rs.getString(3));
                po.setTimestamp(rs.getTimestamp(4).toString().replace(".0", ""));
                messages.add(po);
            }
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit(messages);
        }

        return messages;
    }

    private List<MessagePO> processPrivateResults(PreparedStatement stmt) {
        Log.enter(stmt);

        if (stmt == null) {
            Log.warn("Inside processMessageById method with NULL statement object.");
            return null;
        }

        Log.debug("Executing stmt = " + stmt);
        List<MessagePO> messages = new ArrayList<MessagePO>();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                MessagePO po = new MessagePO();
                po = new MessagePO();
                po.setContent(rs.getString(1));
                po.setAuthor(rs.getString(2));
                po.setTarget(rs.getString(3));
                po.setTimestamp(rs.getTimestamp(4).toString());
                messages.add(po);
            }
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit(messages);
        }

        return messages;
    }

    /**
     * This method will load all the users in the
     * database.
     *
     * @return - List of messages.
     */
    public List<UserPO> loadChatBuddies(String author) {
        if (author == null) {
            Log.warn("Inside findByName method with NULL author.");
            return null;
        }

        Set<UserPO> po = new HashSet<UserPO>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn
                     .prepareStatement(SQL.GET_CHAT_BUDDIES)) {
            stmt.setString(1, author);
            po.addAll(processChatBuddies(stmt));
        } catch (SQLException e) {
            handleException(e);
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn
                     .prepareStatement(SQL.GET_CHAT_BUDDIES2)) {
            stmt.setString(1, author);
            po.addAll(processChatBuddies(stmt));
        } catch (SQLException e) {
            handleException(e);
        }

        return new ArrayList<UserPO>(po);
    }

    private List<UserPO> processChatBuddies(PreparedStatement stmt) {
        Log.enter(stmt);

        if (stmt == null) {
            Log.warn("Inside processChatBuddies method with NULL statement object.");
            return null;
        }

        Log.debug("Executing stmt = " + stmt);
        List<UserPO> users = new ArrayList<UserPO>();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                UserPO po = new UserPO();
                po.setUserName(rs.getString(1));
                users.add(po);
            }
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit(users);
        }

        return users;
    }

    /**
     * This method will load all the users in the
     * database.
     *
     * @return - List of messages.
     */
    public String loadMessageById(int id) {
        Log.enter(id);

//        if (id == null) {
//            Log.warn("Inside loadMessageById method with NULL id.");
//            return null;
//        }

        MessagePO po = null;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn
                     .prepareStatement(SQL.GET_MESSAGE_BY_ID)) {
            stmt.setInt(1, id);

            po = processMessageById(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(po);
        }

        return po.getContent();
    }

    private MessagePO processMessageById(PreparedStatement stmt) {
        Log.enter(stmt);

        if (stmt == null) {
            Log.warn("Inside processMessageById method with NULL statement object.");
            return null;
        }

        Log.debug("Executing stmt = " + stmt);
        MessagePO po = new MessagePO();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                po.setContent(rs.getString(1));
            }
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit(po);
        }

        return po;
    }

    public TestResult getTestResult() {
        TestResult tr = new TestResult();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn
                     .prepareStatement(SQL.GET_TEST_RESULTS)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                float gets = rs.getInt(1);
                float posts = rs.getInt(2);
                float time = rs.getInt(3);
                tr.setGet(gets / time);
                tr.setPost(posts / time);
            }
        } catch (SQLException e) {
            handleException(e);
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn
                     .prepareStatement(SQL.DROP_TEST_GET)) {
            stmt.execute();
        } catch (SQLException e) {
            handleException(e);
        }
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn
                     .prepareStatement(SQL.DROP_TEST_POST)) {
            stmt.execute();
        } catch (SQLException e) {
            handleException(e);
        }
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn
                     .prepareStatement(SQL.DROP_TEST_RESULTS)) {
            stmt.execute();
        } catch (SQLException e) {
            handleException(e);
        }

        return tr;
    }

    public void startTest(int seconds) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn
                     .prepareStatement(SQL.CREATE_TEST_GET)) {
            stmt.execute();
        } catch (SQLException e) {
            handleException(e);
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn
                     .prepareStatement(SQL.CREATE_TEST_POST)) {
            stmt.execute();
        } catch (SQLException e) {
            handleException(e);
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn
                     .prepareStatement(SQL.INSERT_INTO_TEST1)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            handleException(e);
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn
                     .prepareStatement(SQL.INSERT_INTO_TEST2)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            handleException(e);
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn
                     .prepareStatement(SQL.INSERT_INTO_TEST3)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            handleException(e);
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn
                     .prepareStatement(SQL.CREATE_TEST_RESULTS)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            handleException(e);
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn
                     .prepareStatement(SQL.INSERT_TIME_INTO_TEST)) {

            Date date = new Date();
            date.setTime(date.getTime() + seconds * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            stmt.setTimestamp(1, Timestamp.valueOf(sdf.format(date)));
            stmt.setInt(2, seconds);

            stmt.executeUpdate();
        } catch (SQLException e) {
            handleException(e);
        }
    }


    public List<MessagePO> searchWall(String content) {
        Log.enter();

        String query = SQL.SEARCH_WALL;

        List<MessagePO> messages = new ArrayList<MessagePO>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setString(1, "%" + content + "%");
            messages = processPublicResults(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(messages);
        }
        return messages;
    }

    public List<MessagePO> searchAnnouncements(String content) {
        Log.enter();

        String query = SQL.SEARCH_ANNOUNCEMENTS;

        List<MessagePO> messages = new ArrayList<MessagePO>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setString(1, "%" + content + "%");
            messages = processPublicResults(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(messages);
        }
        return messages;
    }

    public List<MessagePO> searchPM(String content, String username) {
        Log.enter();

        String query = SQL.SEARCH_PM;

        List<MessagePO> messages = new ArrayList<MessagePO>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setString(1, "%" + content + "%");
            stmt.setString(2, username);
            stmt.setString(3, username);
            messages = processPrivateResults(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(messages);
        }
        return messages;
    }
}
