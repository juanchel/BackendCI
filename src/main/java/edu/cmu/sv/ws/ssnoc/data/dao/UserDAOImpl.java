package edu.cmu.sv.ws.ssnoc.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;

/**
 * DAO implementation for saving User information in the H2 database.
 *
 */
public class UserDAOImpl extends BaseDAOImpl implements IUserDAO {
    /**
     * This method will load users from the DB with specified account status. If
     * no status information (null) is provided, it will load all users.
     *
     * @return - List of users
     */
    public List<UserPO> loadUsers() {
        Log.enter();

        String query = SQL.FIND_ALL_USERS;

        List<UserPO> users = new ArrayList<UserPO>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            users = processResults(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(users);
        }

        return users;
    }

    private List<UserPO> processResults(PreparedStatement stmt) {
        Log.enter(stmt);

        if (stmt == null) {
            Log.warn("Inside processResults method with NULL statement object.");
            return null;
        }

        Log.debug("Executing stmt = " + stmt);
        List<UserPO> users = new ArrayList<UserPO>();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                UserPO po = new UserPO();
                po.setUserId(rs.getLong(1));
                po.setUserName(rs.getString(2));
                po.setPassword(rs.getString(3));
                po.setEmergency_status(rs.getInt(4));
                po.setSalt(rs.getString(5));
                po.setRole(rs.getInt(6));

                users.add(po);
            }
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit(users);
        }

        return users;
    }

    private List<UserPO> processResultsWithActive(PreparedStatement stmt) {
        Log.enter(stmt);

        if (stmt == null) {
            Log.warn("Inside processResults method with NULL statement object.");
            return null;
        }

        Log.debug("Executing stmt = " + stmt);
        List<UserPO> users = new ArrayList<UserPO>();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                UserPO po = new UserPO();
                po.setUserId(rs.getLong(1));
                po.setUserName(rs.getString(2));
                po.setPassword(rs.getString(3));
                po.setEmergency_status(rs.getInt(4));
                po.setSalt(rs.getString(5));
                po.setRole(rs.getInt(6));
                po.setActive(rs.getBoolean(7));

                users.add(po);
            }
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit(users);
        }

        return users;
    }

    private String processStatusResults(PreparedStatement stmt) {
        String status = null;
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                status = rs.getString(1);
            }
        } catch (SQLException e) {
            handleException(e);
        } finally {

        }
        return status;
    }

    /**
     * This method with search for a user by his userName in the database. The
     * search performed is a case insensitive search to allow case mismatch
     * situations.
     *
     * @param userName
     *            - User name to search for.
     *
     * @return - UserPO with the user information if a match is found.
     */
    @Override
    public UserPO findByName(String userName) {
        Log.enter(userName);

        if (userName == null) {
            Log.warn("Inside findByName method with NULL userName.");
            return null;
        }

        UserPO po = null;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn
                     .prepareStatement(SQL.FIND_USER_BY_NAME)) {
            stmt.setString(1, userName.toUpperCase());

            List<UserPO> users = processResultsWithActive(stmt);

            if (users.size() == 0) {
                Log.debug("No user account exists with userName = " + userName);
            } else {
                po = users.get(0);
            }
        } catch (SQLException e) {
            handleException(e);
            Log.exit(po);
        }

        return po;
    }

    @Override
    public String getStatusByName(String userName) {
        Log.enter(userName);

        if (userName == null) {
            Log.warn("Inside findByName method with NULL userName.");
            return null;
        }

        String status = null;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn
                     .prepareStatement(SQL.FIND_STATUS_BY_NAME)) {
            stmt.setString(1, userName.toUpperCase());

            status = processStatusResults(stmt);

        } catch (SQLException e) {
            handleException(e);
        }

        return status;
    }

    /**
     * This method will save the information of the user into the database.
     *
     * @param userPO
     *            - User information to be saved.
     */
    @Override
    public void save(UserPO userPO) {
        Log.enter(userPO);
        if (userPO == null) {
            Log.warn("Inside save method with userPO == NULL");
            return;
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.INSERT_USER)) {
            stmt.setString(1, userPO.getUserName());
            stmt.setString(2, userPO.getPassword());
            stmt.setInt(3, userPO.getEmergency_status());
            stmt.setString(4, userPO.getSalt());

            int rowCount = stmt.executeUpdate();
            Log.trace("Statement executed, and " + rowCount + " rows inserted.");
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit();
        }
    }

    public void updatePW(String username, String pw, String salt) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.UPDATE_PASSWORD)) {
            stmt.setString(1, pw);
            stmt.setString(2, salt);
            stmt.setString(3, username);

            stmt.executeUpdate();
        } catch (SQLException e) {
            handleException(e);
        } finally {

        }
    }

    public void updateUsername(String username, String nextName) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.UPDATE_USERNAME)) {
            stmt.setString(1, nextName);
            stmt.setString(2, username);

            stmt.executeUpdate();
        } catch (SQLException e) {
            handleException(e);
        } finally {

        }
    }

    public void updateRole(String username, int role) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.UPDATE_ROLE)) {
            stmt.setInt(1, role);
            stmt.setString(2, username);

            stmt.executeUpdate();
        } catch (SQLException e) {
            handleException(e);
        } finally {

        }
    }


    public void updateActive(String username, boolean active) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.UPDATE_ACTIVE)) {
            stmt.setBoolean(1, active);
            stmt.setString(2, username);

            stmt.executeUpdate();
        } catch (SQLException e) {
            handleException(e);
        } finally {

        }
    }

    @Override
    public void updateStatus(String username, int status) {
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL.UPDATE_STATUS)) {
            stmt.setInt(1, status);
            stmt.setString(2, username);

            stmt.executeUpdate();
        } catch (SQLException e) {
            handleException(e);
        } finally {
            
        }

    }

    public List<UserPO> searchUsername(String userName) {
        Log.enter();

        String query = SQL.SEARCH_USERNAME;

        List<UserPO> users = new ArrayList<UserPO>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setString(1, "%"+userName+"%");
            users = processResults(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(users);
        }

        return users;
    }

    public List<UserPO> searchStatus(int status) {
        Log.enter();

        String query = SQL.SEARCH_STATUS;

        List<UserPO> users = new ArrayList<UserPO>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setInt(1, status);
            users = processResults(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(users);
        }

        return users;
    }

}
